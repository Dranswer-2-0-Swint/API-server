package com.t3q.dranswer.service;

import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.dto.cman.*;
import com.t3q.dranswer.dto.db.DbContainer;
import com.t3q.dranswer.dto.db.DbImage;
import com.t3q.dranswer.dto.db.DbMicroDomain;
import com.t3q.dranswer.dto.servpot.*;
import com.t3q.dranswer.mapper.ImageMapper;
import com.t3q.dranswer.mapper.MicroDomainMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Log4j2
@Service
public class ImageService {
	
	@Autowired
	ImageMapper imageMapper;

	@Autowired
	MicroDomainMapper microDomainMapper;

	private final RestTemplate restTemplate;
	private final ApplicationProperties applicationProperties;
	
	@Autowired
    public ImageService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }
    
	public ServpotImageListReadRes readImageList(String micro) throws Exception {
		log.info("ImageService : readImageList");
		List<DbImage> dbImageList = imageMapper.selectImageByMicro(micro);
		if (dbImageList == null) {
			throw new Exception(Constants.E40004);
		}

		ServpotImageListReadRes res = new ServpotImageListReadRes();
		res.setImageList(new ArrayList<>());
		res.setMicroId(micro);
		for (DbImage dbImage : dbImageList) {
			ServpotImageListReadResSub sub = new ServpotImageListReadResSub();
			sub.setImageId(dbImage.getImage());
			sub.setImageName(dbImage.getImageName());
			res.getImageList().add(sub);
		}

		return res;
	}
	
	public ServpotImageReadRes readImage(String image) throws Exception {
		log.info("ImageService : readImage");
		ServpotImageReadRes res = new ServpotImageReadRes();
		DbImage dbImage = imageMapper.selectImage(image);
		if (dbImage == null || dbImage.getImage() == null) {
			throw new Exception(Constants.E40004);
		}

		List<DbContainer> dbContainerList = imageMapper.selectContainerByImage(image);
		String container = imageMapper.selectContainerIdByImage(image);
		
		res.setImageDomainList(new ArrayList<>());
		res.setMicroId(dbImage.getMicroService());
		res.setImageId(dbImage.getImage());
		res.setImageName(dbImage.getImageName());

		if (container != null && !dbImage.getImageStatus().equals(Constants.STATUS_IMAGE_UPLOAD_FAILED)) {
			for (DbContainer dbCon : dbContainerList) {
				ServpotImageReadResSub sub = new ServpotImageReadResSub();
				sub.setImageDomain(dbCon.getDomain());
				res.getImageDomainList().add(sub);
			}
	
			try {
				// 컨테이너 사긴정보 조회
				CmanContainerTimeReadRes cmanRes = getContainerTimeInfo(container);
				res.setRegistTime(cmanRes.getCreateTime());
				res.setModifyTime(cmanRes.getLastUpdateTime());
				res.setStartTime(cmanRes.getLastDeployTime());
				res.setStopTime(cmanRes.getLastRecycleTime());
				res.setRunningTime(String.valueOf(cmanRes.getTotalAge()));
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					imageMapper.updateImageStatus(image, Constants.STATUS_SEARCH_FAILED, Constants.DETAIL_IMAGE_NOT_EXIST);
					throw new Exception(Constants.E40004);
				}
				throw new Exception(e.getMessage());
			}
		} else {
			res.setRegistTime(Constants.ZERO);
			res.setModifyTime(Constants.ZERO);
			res.setStartTime(Constants.ZERO);
			res.setStopTime(Constants.ZERO);
			res.setRunningTime(Constants.ZERO);
		}
		
		return res;
	}

	public ServpotImageStatusRes readImageStatus(String image) throws Exception {
		log.info("ImageService : readImageStatus");
		String pod = "";
		DbImage dbImage = imageMapper.selectImage(image);
		String container = imageMapper.selectContainerIdByImage(image);
		
		if (dbImage == null) {
			throw new Exception(Constants.E40004);
		}
		
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		ServpotImageStatusRes res = new ServpotImageStatusRes();
		res.setImageId(image);
		
		if (container != null) {
			if (!dbImage.getImageStatus().equals(Constants.STATUS_DEPLOY_FAILED) && !dbImage.getImageStatus().equals(Constants.STATUS_SEARCH_FAILED)) {
				try {
					// 1. 컨테이너 조회
					CmanContainerReadRes cmanRes = getContainerInfo(container);
					log.info("container name : " + cmanRes.getName());
					List<DbContainer> containerList = new ArrayList<>();
					for (String innerDomain : cmanRes.getInnerDomain()) {
						DbContainer dbContainer = new DbContainer();
						dbContainer.setImage(image);
						dbContainer.setContainer(container);
						dbContainer.setDomain(innerDomain);
						containerList.add(dbContainer);
					}
					imageMapper.removeContainerByImage(image);
					imageMapper.insertContainer(containerList);
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
						imageMapper.updateImageStatus(image, Constants.STATUS_SEARCH_FAILED, Constants.DETAIL_CONTAINER_NOT_EXIST);
						throw new Exception(Constants.E40004);
					}
					throw new Exception(e.getMessage());
				}
				
				try {
					// 2. 파드 이름 조회
					CmanContainerPodReadRes cmanRes = getContainerPod(service, container);
					pod = cmanRes.getPodList().get(0).getPodName();
					log.info("container pod name : " + pod);

				} catch (Exception e) {
					//e.printStackTrace();
					log.error(e.getMessage());
					if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
						imageMapper.updateImageStatus(image, Constants.STATUS_POD_STOPPED, Constants.DETAIL_SHUTDOWN);
					} else {
						throw new Exception(e.getMessage());
					}
				}
			}
		}
		
		if (!pod.isEmpty()) {
			try {
				// 3. 파드 상태 조회
				CmanContainerStatusReadRes cmanRes = getContainerStatus(service, container, pod);
				
				String state = cmanRes.getState(); 
				switch (state) {
					case Constants.DETAIL_RUNNING :
						dbImage.setImageStatus(Constants.STATUS_POD_RUNNING);
						dbImage.setImageStatusDetail(state);
						break;
					case Constants.DETAIL_DEPLOYING :
						dbImage.setImageStatus(Constants.STATUS_POD_DEPLOYING);
						dbImage.setImageStatusDetail(state);
						break;
					case Constants.DETAIL_TERMINATING :
						dbImage.setImageStatus(Constants.STATUS_POD_TERMINATING);
						dbImage.setImageStatusDetail(state);
						break;
					case Constants.DETAIL_ERROR_IMAGE_PULL :
					case Constants.DETAIL_ERROR_OOM :
					case Constants.DETAIL_ERROR_PENDING :
					case Constants.DETAIL_ERROR_CRASHED :
					case Constants.DETAIL_ERROR_INTERNAL_ERR :
						dbImage.setImageStatus(Constants.STATUS_DEPLOY_FAILED);
						dbImage.setImageStatusDetail(state);
						break;
					default : 
						break;
				}
				imageMapper.updateImageStatus(image, dbImage.getImageStatus(), dbImage.getImageStatusDetail());

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				if (e.getMessage().equals(Constants.E50002)) {
					dbImage.setImageStatus(Constants.STATUS_POD_STOPPED);
					dbImage.setImageStatusDetail(Constants.DETAIL_SHUTDOWN);
					imageMapper.updateImageStatus(image, dbImage.getImageStatus(), dbImage.getImageStatusDetail());
				}
				throw new Exception(e.getMessage());
			}
		}
		
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());

		return res;
	}

	public ServpotImageStatusRes updateImageStatus(ServpotImageStatusUpdateReq imageReq) throws Exception {
		log.info("ImageService : updateImageStatus");
		String pod = "";
		DbImage dbImage = imageMapper.selectImage(imageReq.getImageId());
		if (dbImage == null) {
			throw new Exception(Constants.E40004);
		}
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		if (service == null) {
			throw new Exception(Constants.E40004);
		}
		String container = imageMapper.selectContainerIdByImage(imageReq.getImageId());
		List<DbMicroDomain> microDomains = microDomainMapper.selectMicroDomainByMicro(dbImage.getMicroService());

		ServpotImageStatusRes res = new ServpotImageStatusRes();
		res.setImageId(imageReq.getImageId());

		if (container != null && !dbImage.getImageStatus().equals(Constants.STATUS_SEARCH_FAILED)) {
			try {
				// 1. 컨테이너 조회
				CmanContainerReadRes cmanRes = getContainerInfo(container);
				log.info("container name : " + cmanRes.getName());

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					imageMapper.updateImageStatus(dbImage.getImage(), Constants.STATUS_SEARCH_FAILED, Constants.DETAIL_CONTAINER_NOT_EXIST);
					throw new Exception(Constants.E40004);
				}
				throw new Exception(e.getMessage());
			}

			try {
				// 2. 파드 이름 조회
				CmanContainerPodReadRes cmanRes = getContainerPod(service, container);
				pod = cmanRes.getPodList().get(0).getPodName();
				log.info("container pod name : " + pod);

			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage());
				if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					imageMapper.updateImageStatus(dbImage.getImage(), Constants.STATUS_POD_STOPPED, Constants.DETAIL_SHUTDOWN);
				} else {
					throw new Exception(e.getMessage());
				}
			}

			if (pod.isEmpty() && imageReq.getImageStatus().equals(Constants.STATUS_RUN)) {
				imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_POD_DEPLOYING, Constants.DETAIL_DEPLOYING);
				try {
					CmanContainerDeployReq cmanDeployReq = new CmanContainerDeployReq();
					cmanDeployReq.setDomains(new ArrayList<>());
					if (!microDomains.isEmpty()) {
						List<DbContainer> dbContainerList = imageMapper.selectContainerByImage(imageReq.getImageId());
						for(DbContainer dbContainer : dbContainerList) {
							Optional<DbMicroDomain> dbMicroDomain = microDomains.stream().filter(s -> s.getPort() == dbContainer.getPort()).findFirst();
							if (dbMicroDomain.isPresent()) {
								CmanContainerDeployReqSub cmanContainerDomainSub = new CmanContainerDeployReqSub();
								cmanContainerDomainSub.setDomain(dbMicroDomain.get().getDomain());
								cmanContainerDomainSub.setPort(dbMicroDomain.get().getPort());
								cmanContainerDomainSub.setPath(dbMicroDomain.get().getPath());
								cmanDeployReq.setHas_domain(true);
								cmanDeployReq.getDomains().add(cmanContainerDomainSub);
							}
						}
					}

					CmanContainerDeployRes cmanDeployRes = setContainerDeploy(service, container, cmanDeployReq);
					log.info("container deploy success.\nenvName : " + cmanDeployRes.getEnvName());
					dbImage.setImageStatus(Constants.STATUS_POD_DEPLOYING);
					dbImage.setImageStatusDetail(Constants.DETAIL_DEPLOYING);
					imageMapper.updateImageStatus(imageReq.getImageId(), dbImage.getImageStatus(), dbImage.getImageStatusDetail());

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					String msg = e.getMessage();
					switch (msg) {
						case Constants.DETAIL_ERROR_NOT_EXIST:
							imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_SEARCH_FAILED, Constants.DETAIL_CONTAINER_NOT_EXIST);
							throw new Exception(Constants.E40004);
						case Constants.DETAIL_ERROR_DUPLICATED:
							imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_REGIST_FAILED, Constants.DETAIL_DOMAIN_DUPLICATED);
							throw new Exception(Constants.E40004);
						case Constants.DETAIL_ERROR_IMAGE_PULL:
						case Constants.DETAIL_ERROR_OOM:
						case Constants.DETAIL_ERROR_PENDING:
						case Constants.DETAIL_ERROR_CRASHED:
						case Constants.DETAIL_ERROR_INTERNAL_ERR:
							imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_DEPLOY_FAILED, msg);
							throw new Exception(Constants.E50002);
					}
					imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_DEPLOY_FAILED, Constants.DETAIL_ERROR_INTERNAL_ERR);
					throw new Exception(e.getMessage());
				}
			} else if (!pod.isEmpty() && imageReq.getImageStatus().equals(Constants.STATUS_SUSPEND)) {

				imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_POD_TERMINATING, Constants.DETAIL_TERMINATING);

				try {

					CmanContainerRecycleRes cmanRes = setContainerRecycle(service, container);
					log.info("container recycle success.\nmessage : " + cmanRes.getMessage());
					dbImage.setImageStatus(Constants.STATUS_POD_TERMINATING);
					dbImage.setImageStatusDetail(Constants.DETAIL_TERMINATING);
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					if (e.getMessage().equals(Constants.E50002)) {
						imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_DEPLOY_FAILED, Constants.DETAIL_ERROR_INTERNAL_ERR);
					}
					throw new Exception(e.getMessage());
				}
			}
		}
		
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());
		
		return res;
	}
	
	public ServpotImageRegistRes createImageRegist(ServpotImageRegistReq imageReq) {
		log.info("ImageService : createImageRegist");
		// 이미지ID 생성
		String imageId = Constants.PREFIX_IMG + HashUtil.makeCRC32(imageMapper.getImageSequence());

		// 이미지 DB 저장
		DbImage dbImage = new DbImage();
		dbImage.setImage(imageId);
		dbImage.setMicroService(imageReq.getMicroId());
		dbImage.setImageName(imageReq.getImageName());
		dbImage.setImageStatus(Constants.STATUS_IMAGE_UPLOADING);
		dbImage.setImageStatusDetail(Constants.DETAIL_UPLOADING);
		imageMapper.insertImage(dbImage);

		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		// 비동기 호출 
		CompletableFuture.runAsync(() -> {
			try {
				asyncImageRegist(localdata, imageId, imageReq);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// 응답 생성
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		res.setImageId(dbImage.getImage());
		res.setImageName(dbImage.getImageName());
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());
		
		return res;
	}

	public ServpotImageRegistRes updateImageRegist(ServpotImageRegistUpdateReq imageReq) throws Exception {
		log.info("ImageService : updateImageRegist");
		DbImage dbImage = imageMapper.selectImage(imageReq.getImageId());
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		String container = imageMapper.selectContainerIdByImage(imageReq.getImageId());
		
		if (dbImage == null || service == null || container == null) {
			throw new Exception(Constants.E40004);
		}

		ServpotImageRegistRes res = new ServpotImageRegistRes();
		res.setImageId(imageReq.getImageId());
		res.setImageName(imageReq.getImageName());
		
		CmanContainerUpdateReq cmanContainerReq = new CmanContainerUpdateReq();
		cmanContainerReq.setEnv(new ArrayList<>());
		cmanContainerReq.setServicePort(new ArrayList<>());
		cmanContainerReq.setVolumeMounts(new ArrayList<>());
		cmanContainerReq.setProjectName(service);
		
		List<ServpotImageRegistUpdateReqSub> setupList = imageReq.getSetupList()
																.stream()
																.filter(set -> set.getSetupType().equals(Constants.TYPE_OPEN))
																.collect(Collectors.toList());
		for (ServpotImageRegistUpdateReqSub setup : setupList) {
			String[] value = setup.getSetupValue().split(Constants.SLASH);
			CmanContainerUpdateReqPort sub = new CmanContainerUpdateReqPort();
			sub.setPort(Integer.parseInt(value[0]));
			sub.setProtocol(value[1]);
			cmanContainerReq.getServicePort().add(sub);
		}
		setupList.clear();
		setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_MOUNT)).collect(Collectors.toList());
		for (ServpotImageRegistUpdateReqSub setup : setupList) {
			String[] value = setup.getSetupValue().split(Constants.COLONS);
			CmanContainerUpdateReqVol sub = new CmanContainerUpdateReqVol();
			sub.setName(String.valueOf(setup.getSetupSeq()));
			sub.setPath(value[0]);
			sub.setMountPath(value[1]);
			cmanContainerReq.getVolumeMounts().add(sub);
		}
		setupList.clear();
		setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_ENV)).collect(Collectors.toList());
		for (ServpotImageRegistUpdateReqSub setup : setupList) {
			CmanContainerUpdateReqEnv sub = new CmanContainerUpdateReqEnv();
			sub.setName(setup.getSetupKey());
			sub.setValue(setup.getSetupValue());
			cmanContainerReq.getEnv().add(sub);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		
		HttpEntity<CmanContainerUpdateReq> containerEntity = new HttpEntity<>(cmanContainerReq, headers);
		URI uri = UriComponentsBuilder
			    .fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_UPDATE_URL)
			    .encode()
			    .buildAndExpand(container)
			    .toUri();
		
		try {
			ResponseEntity<CmanContainerUpdateRes> cmanContainerRes = restTemplate.exchange(	uri, 
																								HttpMethod.PATCH, 
																								containerEntity, 
																								CmanContainerUpdateRes.class);
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}

		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());

		return res;
	}

	public ServpotImageDeleteRes deleteImage(String image) throws Exception {
		log.info("ImageService : deleteImage");
		DbImage dbImage = imageMapper.selectImage(image);
		if (dbImage == null) {
			throw new Exception(Constants.E40004);
		}
		
		ServpotImageDeleteRes res = new ServpotImageDeleteRes();
		res.setImageId(dbImage.getImage());
		
		String service = imageMapper.selectServiceByImage(dbImage.getImage());
		String container = imageMapper.selectContainerIdByImage(dbImage.getImage());

		if (container != null && !dbImage.getImageStatus().equals(Constants.STATUS_IMAGE_UPLOAD_FAILED)) {
			try {
				switch (dbImage.getImageStatus()) {
					case Constants.STATUS_POD_RUNNING :
					case Constants.STATUS_DEPLOY_FAILED :
						setContainerRecycle(service, container);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				if (e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					imageMapper.updateImageStatus(dbImage.getImage(), Constants.STATUS_DEPLOY_FAILED, Constants.DETAIL_ERROR_INTERNAL_ERR);
				} else {
					throw new Exception(e.getMessage());
				}
			}
		}

/*		논리삭제
		try {

			List<CmanImageReadRes> cmanRes = getImage(service, dbImage.getMicroService());

			if (cmanRes.size() > 0) {
				final String imageName = dbImage.getImage();
				CmanImageReadRes imageInfo = cmanRes.get(0);
				boolean match = imageInfo.getTags().stream().anyMatch(tag -> tag.getName().equals(imageName));

				if (match) {
					CmanContainerDeleteRes cmanImageTagRes = delImageTag(service, dbImage.getMicroService(), image);
					log.info("image delete success : " + cmanImageTagRes.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (!e.getMessage().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
				throw new Exception(e.getMessage());
			}
		}
*/

		imageMapper.deleteContainerByImage(dbImage.getImage());
		imageMapper.deleteImage(dbImage.getImage());

		return res;
	}

	public void asyncImageRegist(RequestContext.RequestContextData localdata, String imageId, ServpotImageRegistReq imageReq) throws Exception {
		MDC.put("requestId", localdata.getRequestId());
		log.info("ImageService : asyncImageRegist");
		String service = imageMapper.selectServiceByMicro(imageReq.getMicroId());
		
		CmanImageRegistReq cmanImageReq = new CmanImageRegistReq();
		cmanImageReq.setProjectName(service);
		cmanImageReq.setFileName(imageReq.getImagePath());
		cmanImageReq.setRepository(imageReq.getMicroId());
		cmanImageReq.setTag(imageId);

		HttpHeaders headers = new HttpHeaders();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CmanImageRegistReq> imageEntity = new HttpEntity<>(cmanImageReq, headers);
		URI uri = UriComponentsBuilder
				    .fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_IMAGE_CREATE_URL)
				    .queryParam("remove_temp", "{remove_temp}")
				    .encode()
				    .buildAndExpand("false")
				    .toUri();

		String imageName = "";
		String containerId = "";

		try {
			ResponseEntity<CmanImageRegistRes> cmanImageRes = restTemplate.exchange(uri, 
																					HttpMethod.POST, 
																					imageEntity, 
																					CmanImageRegistRes.class);
			
			if (cmanImageRes.getStatusCode() == HttpStatus.OK) {
				log.info("imagename : " + cmanImageRes.getBody().getImage_name());

				// 컨테이너ID 생성
				containerId = Constants.PREFIX_CON + HashUtil.makeCRC32(imageMapper.getContainerSequence());
				String[] pushCommand = cmanImageRes.getBody().getPush_command().split(Constants.SPACE);
				imageName = pushCommand[2];
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				String status = errRes.getMsg();
				if (status.equals(Constants.DETAIL_ERROR_LOAD) || status.equals(Constants.DETAIL_ERROR_PUSH)) {
					imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, status);
				} else if (status.equals(Constants.DETAIL_ERROR_DUPLICATED)) {
					imageMapper.updateImageStatus(imageId, Constants.STATUS_REGIST_FAILED, Constants.DETAIL_IMAGE_DUPLICATED);
				}
			} else {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
			throw new Exception(Constants.E50000);
		}

		// 이미지(harbor) 변경
		imageMapper.updateImageRealName(imageId, imageName);

		CmanContainerCreateReq cmanContainerReq = new CmanContainerCreateReq();
		cmanContainerReq.setEnv(new ArrayList<>());
		cmanContainerReq.setServicePort(new ArrayList<>());
		cmanContainerReq.setVolumeMounts(new ArrayList<>());
		cmanContainerReq.setProjectName(service);
		cmanContainerReq.setConName(containerId);
		cmanContainerReq.setImage(imageName);

		List<ServpotImageRegistReqSub> setupList = imageReq.getSetupList()
															.stream()
															.filter(set -> set.getSetupType().equals(Constants.TYPE_OPEN))
															.collect(Collectors.toList());
		for (ServpotImageRegistReqSub setup : setupList) {
			String[] value = setup.getSetupValue().split(Constants.SLASH);
			CmanContainerCreateReqPort sub = new CmanContainerCreateReqPort();
			sub.setPort(Integer.parseInt(value[0]));
			sub.setProtocol(value[1]);
			cmanContainerReq.getServicePort().add(sub);
		}
		setupList.clear();
		setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_MOUNT)).collect(Collectors.toList());
		for (ServpotImageRegistReqSub setup : setupList) {
			String[] value = setup.getSetupValue().split(Constants.COLONS);
			CmanContainerCreateReqVol sub = new CmanContainerCreateReqVol();
			sub.setName(String.valueOf(setup.getSetupSeq()));
			sub.setPath(value[0]);
			sub.setMountPath(value[1]);
			cmanContainerReq.getVolumeMounts().add(sub);
		}
		setupList.clear();
		setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_ENV)).collect(Collectors.toList());
		for (ServpotImageRegistReqSub setup : setupList) {
			CmanContainerCreateReqEnv sub = new CmanContainerCreateReqEnv();
			sub.setName(setup.getSetupKey());
			sub.setValue(setup.getSetupValue());
			cmanContainerReq.getEnv().add(sub);
		}

		HttpEntity<CmanContainerCreateReq> containerEntity = new HttpEntity<>(cmanContainerReq, headers);
		uri = UriComponentsBuilder
				.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_CREATE_URL)
				.build()
				.encode()
				.toUri();

		try {
			ResponseEntity<CmanContainerCreateRes> cmanContainerRes = restTemplate.exchange(	uri,
																								HttpMethod.POST,
																								containerEntity,
																								CmanContainerCreateRes.class);
				
			if (cmanContainerRes.getStatusCode() == HttpStatus.OK) {
				log.info("imagename : " + cmanContainerRes.getBody().getConName());

				// 이미지(container) 변경
				List<DbContainer> containerList = new ArrayList<>();
				for (String innerDomain : cmanContainerRes.getBody().getInnerDomain()) {
					DbContainer container = new DbContainer();
					container.setImage(imageId);
					container.setContainer(containerId);
					container.setDomain(innerDomain);
					containerList.add(container);
				}
				imageMapper.insertContainer(containerList);
				imageMapper.updateImageStatus(imageId, Constants.STATUS_POD_STOPPED, Constants.DETAIL_SHUTDOWN);
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_DUPLICATED)) {
					imageMapper.updateImageStatus(imageId, Constants.STATUS_REGIST_FAILED, Constants.DETAIL_CONTAINER_DUPLICATED);
				}
			} else {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
		}
	}


	public CmanContainerReadRes getContainerInfo(String container) throws Exception {
		CmanContainerReadRes res = new CmanContainerReadRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_READ_URL)
				    .encode()
				    .buildAndExpand(container)
				    .toUri();
		try {
			ResponseEntity<CmanContainerReadRes> cmanRes = restTemplate.exchange(	uri,
																					HttpMethod.GET, 
																					entity, 
																					CmanContainerReadRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}
	
	public CmanContainerTimeReadRes getContainerTimeInfo(String container) throws Exception {
		CmanContainerTimeReadRes res = new CmanContainerTimeReadRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_TIME_READ_URL)
			    	.encode()
			    	.buildAndExpand(container)
			    	.toUri();
		try {
			ResponseEntity<CmanContainerTimeReadRes> cmanRes = restTemplate.exchange(	uri, 
																						HttpMethod.GET, 
																						entity, 
																						CmanContainerTimeReadRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}
	
	public CmanContainerPodReadRes getContainerPod(String service, String container) throws Exception {
		CmanContainerPodReadRes res = new CmanContainerPodReadRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_POD_READ_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(container, service)
				    .toUri();
		try {
			ResponseEntity<CmanContainerPodReadRes> cmanRes = restTemplate.exchange(uri, 
																					HttpMethod.GET, 
																					entity, 
																					CmanContainerPodReadRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			//e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}

		return res;
	}

	public CmanContainerStatusReadRes getContainerStatus(String service, String container, String pod) throws Exception {
		CmanContainerStatusReadRes res = new CmanContainerStatusReadRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_STATUS_READ_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(container, pod, service)
				    .toUri();
		try {
			ResponseEntity<CmanContainerStatusReadRes> cmanRes = restTemplate.exchange(	uri, 
																						HttpMethod.GET, 
																						entity, 
																						CmanContainerStatusReadRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}

		return res;
	}

	public CmanContainerDeployRes setContainerDeploy(String service, String container, CmanContainerDeployReq cmanReq) throws Exception {
		CmanContainerDeployRes res = new CmanContainerDeployRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());

		HttpEntity<CmanContainerDeployReq> entity = new HttpEntity<>(cmanReq, headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DEPLOY_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(container, service)
				    .toUri();
		try {
			ResponseEntity<CmanContainerDeployRes> cmanRes = restTemplate.exchange(	uri, 
																					HttpMethod.POST, 
																					entity, 
																					CmanContainerDeployRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				throw new Exception(errRes.getStatus_code());
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}

	public CmanContainerRecycleRes setContainerRecycle(String service, String container) throws Exception {
		CmanContainerRecycleRes res = new CmanContainerRecycleRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_RECYCLE_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(container, service)
				    .toUri();
		try {
			ResponseEntity<CmanContainerRecycleRes> cmanRes = restTemplate.exchange(uri, 
																					HttpMethod.POST, 
																					entity, 
																					CmanContainerRecycleRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}

	//요거는 지우지 말아봐 씨맨에서 지운거
	public CmanContainerDomainCreateDeleteRes setContainerDomain(String service, String container, String domain) throws Exception {
		CmanContainerDomainCreateDeleteRes res = new CmanContainerDomainCreateDeleteRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		CmanContainerDomainCreateReq cmanReq = new CmanContainerDomainCreateReq();
		//cmanReq.setDomainName(domain);
		//cmanReq.setPort(80);		// fix
		HttpEntity<CmanContainerDomainCreateReq> entity = new HttpEntity<>(cmanReq, headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DOMAIN_CREATE_URL)
				    .queryParam("projectName", "{projectName}")
			    	.encode()
			    	.buildAndExpand(container, service)
			    	.toUri();
		try {
			ResponseEntity<CmanContainerDomainCreateDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																								HttpMethod.POST, 
																								entity, 
																								CmanContainerDomainCreateDeleteRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}

	public CmanContainerDeleteRes delContainer(String container) throws Exception {
		CmanContainerDeleteRes res = new CmanContainerDeleteRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DELETE_URL)
				    .encode()
				    .buildAndExpand(container)
				    .toUri();
		try {
			ResponseEntity<CmanContainerDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																					HttpMethod.DELETE, 
																					entity, 
																					CmanContainerDeleteRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}

	//요거는 지우지 말아봐 씨맨에서 지운거
	public CmanContainerDomainCreateDeleteRes delContainerDomain(String service, String container) throws Exception {
		CmanContainerDomainCreateDeleteRes res = new CmanContainerDomainCreateDeleteRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DOMAIN_DELETE_URL)
				    .queryParam("projectName", "{projectName}")
			    	.encode()
			    	.buildAndExpand(container, service)
			    	.toUri();
		try {
			ResponseEntity<CmanContainerDomainCreateDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																								HttpMethod.DELETE, 
																								entity, 
																								CmanContainerDomainCreateDeleteRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}

		return res;
	}
	
	public List<CmanImageReadRes> getImage(String service, String micro) throws Exception {
		List<CmanImageReadRes> res = new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_IMAGE_READ_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(micro, service)
				    .toUri();
		try {
			//RestTemplate rest = new RestTemplate();
			ResponseEntity<CmanImageReadRes[]> cmanRes = restTemplate.exchange(	uri, 
																				HttpMethod.GET, 
																				entity, 
																				CmanImageReadRes[].class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				log.info("image delete success");
				res = Arrays.asList(cmanRes.getBody());
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}

	public CmanContainerDeleteRes delImageTag(String service, String micro, String image) throws Exception {
		CmanContainerDeleteRes res = new CmanContainerDeleteRes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_IMAGE_TAG_DELETE_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(micro, image, service)
				    .toUri();
		try {
			ResponseEntity<CmanContainerDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																					HttpMethod.DELETE, 
																					entity, 
																					CmanContainerDeleteRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				res = cmanRes.getBody();
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				if (errRes.getMsg().equals(Constants.DETAIL_ERROR_NOT_EXIST)) {
					throw new Exception(Constants.DETAIL_ERROR_NOT_EXIST);
				}
			}
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		return res;
	}
}