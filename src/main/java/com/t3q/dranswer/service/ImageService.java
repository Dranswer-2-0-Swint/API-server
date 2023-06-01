package com.t3q.dranswer.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReq;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqEnv;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqPort;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqVol;
import com.t3q.dranswer.dto.cman.CmanContainerCreateRes;
import com.t3q.dranswer.dto.cman.CmanContainerDeployRes;
import com.t3q.dranswer.dto.cman.CmanContainerPodReadRes;
import com.t3q.dranswer.dto.cman.CmanContainerRecycleRes;
import com.t3q.dranswer.dto.cman.CmanContainerStatusListReadRes;
import com.t3q.dranswer.dto.cman.CmanContainerStatusReadRes;
import com.t3q.dranswer.dto.cman.CmanContainerTimeReadRes;
import com.t3q.dranswer.dto.cman.CmanContainerUpdateReq;
import com.t3q.dranswer.dto.cman.CmanContainerUpdateReqEnv;
import com.t3q.dranswer.dto.cman.CmanContainerUpdateReqPort;
import com.t3q.dranswer.dto.cman.CmanContainerUpdateReqVol;
import com.t3q.dranswer.dto.cman.CmanContainerUpdateRes;
import com.t3q.dranswer.dto.cman.CmanImageRegistReq;
import com.t3q.dranswer.dto.cman.CmanImageRegistRes;
import com.t3q.dranswer.dto.cman.ErrorResponse;
import com.t3q.dranswer.dto.db.DbContainer;
import com.t3q.dranswer.dto.db.DbImage;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadResSub;
import com.t3q.dranswer.dto.servpot.ServpotImageReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageReadResSub;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReqSub;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReqSub;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateReq;
import com.t3q.dranswer.mapper.ImageMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageService {
	
	@Autowired
	ImageMapper imageMapper;
	
	private final RestTemplate restTemplate;
	private final ApplicationProperties applicationProperties;
	
	@Autowired
    public ImageService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }
    
	public ServpotImageListReadRes readImageList(String micro) {
		log.info("ImageService : readImageList");
		List<DbImage> dbImageList = new ArrayList<>();
		dbImageList = imageMapper.selectImageByMicro(micro);
		
		ServpotImageListReadRes res = new ServpotImageListReadRes();
		res.setImageList(new ArrayList<ServpotImageListReadResSub>());
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
		DbImage dbImage = new DbImage(); 
		dbImage = imageMapper.selectImageByimage(image);
		
		List<DbContainer> dbContainerList = new ArrayList<>();
		dbContainerList = imageMapper.selectContainerByImage(image);
		String container = imageMapper.selectContainerIdByImage(image);
		
		res.setImageDomainList(new ArrayList<ServpotImageReadResSub>());
		res.setMicroId(dbImage.getMicroService());
		res.setImageId(dbImage.getImage());
		res.setImageName(dbImage.getImageName());
		for (DbContainer dbCon : dbContainerList) {
			ServpotImageReadResSub sub = new ServpotImageReadResSub();
			sub.setImageDomain(dbCon.getContainerDomain());
			res.getImageDomainList().add(sub);
		}

		if (dbImage == null || dbImage.getImage() == null) {
			throw new Exception(Constants.E40004);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
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
				res.setRegistTime(cmanRes.getBody().getCreateTime());
				res.setModifyTime(cmanRes.getBody().getLastUpdateTime());
				res.setStartTime(cmanRes.getBody().getLastDeployTime());
				res.setStopTime(cmanRes.getBody().getLastRecycleTime());
				res.setRunningTime(cmanRes.getBody().getTotalAge());
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

	public ServpotImageListReadRes readImageListStatus(String micro) {
		log.info("ImageService : readImageList");
		String service = imageMapper.selectServiceByMicro(micro);
		
		List<DbImage> dbImageList = new ArrayList<>();
		dbImageList = imageMapper.selectImageByMicro(micro);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_LIST_STATUS_READ_URL)
			    	.queryParam("projectName", "{projectName}")
			    	.encode()
			    	.buildAndExpand(service)
			    	.toUri();
		try {
			ResponseEntity<List<CmanContainerStatusListReadRes>> cmanRes = restTemplate.exchange(
																			uri, 
																			HttpMethod.GET, 
																			entity, 
																			new ParameterizedTypeReference<List<CmanContainerStatusListReadRes>>() {});
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
//				List<CmanContainerStatusListReadRes> conStatusList = cmanRes.getBody();
//				dbImageList.stream()
//					.forEach(dbImage -> conStatusList.stream()
//						.filter(conStatus -> conStatus.getContainer().equals(dbImage.getContainer()))
//						.findFirst()
//						.ifPresent(conStatus -> dbImage.setImageStatusDetail(ResponseUtil.getStatusCode(conStatus.getState())))
//					);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		
		ServpotImageListReadRes res = new ServpotImageListReadRes();
		res.setImageList(new ArrayList<ServpotImageListReadResSub>());
		res.setMicroId(micro);
		for (DbImage dbImage : dbImageList) {
			ServpotImageListReadResSub sub = new ServpotImageListReadResSub();
			sub.setImageId(dbImage.getImage());
			sub.setImageName(dbImage.getImageName());
			res.getImageList().add(sub);
		}

		return res;
	}

	public ServpotImageStatusRes readImageStatus(String image) throws Exception {
		log.info("ImageService : readImageStatus");
		String pod = new String();
		DbImage dbImage = new DbImage();
		dbImage = imageMapper.selectImageByimage(image);
		String container = imageMapper.selectContainerIdByImage(image);
		
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		ServpotImageStatusRes res = new ServpotImageStatusRes();
		res.setImageId(image);
		
		if (container != null) {
			if (dbImage.getImageStatus() != Constants.STATUS_DEPLOY_FAILED) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
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
						pod = cmanRes.getBody().getPodList().get(0).getPodName();
					}
				} catch (HttpClientErrorException e) {
					e.printStackTrace();
					log.error(e.getMessage());
					dbImage.setImageStatus(Constants.STATUS_POD_STOPPED);
					dbImage.setImageStatusDetail(Constants.DETAIL_SHUTDOWN);
					imageMapper.updateImageStatus(image, dbImage.getImageStatus(), dbImage.getImageStatusDetail());
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					throw new Exception(Constants.E50000);
				}
			}
		}
		
		if (pod.isEmpty() == false) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			URI uri = UriComponentsBuilder
				    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_STATUS_READ_URL)
					    .queryParam("projectName", "{projectName}")
					    .encode()
					    .buildAndExpand(container, pod, service)
					    .toUri();
			try {
				ResponseEntity<CmanContainerStatusReadRes> cmanRes = restTemplate.exchange(uri, 
																						HttpMethod.GET, 
																						entity, 
																						CmanContainerStatusReadRes.class);
				if (cmanRes.getStatusCode() == HttpStatus.OK) {
					String status = cmanRes.getBody().getState(); 
					switch (status) {
						case Constants.DETAIL_RUNNING :
							dbImage.setImageStatus(Constants.STATUS_POD_RUNNING);
							dbImage.setImageStatusDetail(status);
							break;
						case Constants.DETAIL_DEPLOYING :
							dbImage.setImageStatus(Constants.STATUS_POD_DEPLOYING);
							dbImage.setImageStatusDetail(status);
							break;
						case Constants.DETAIL_TERMINATING :
							dbImage.setImageStatus(Constants.STATUS_POD_TERMINATING);
							dbImage.setImageStatusDetail(status);
							break;
						case Constants.DETAIL_ERROR_IMAGE_PULL :
						case Constants.DETAIL_ERROR_OOM :
						case Constants.DETAIL_ERROR_PENDING :
						case Constants.DETAIL_ERROR_CRASHED :
						case Constants.DETAIL_ERROR_INTERNAL_ERR :
							dbImage.setImageStatus(Constants.STATUS_DEPLOY_FAILED);
							dbImage.setImageStatusDetail(status);
							break;
						default : 
							break;
					}
					imageMapper.updateImageStatus(image, dbImage.getImageStatus(), dbImage.getImageStatusDetail());
				}
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				dbImage.setImageStatus(Constants.STATUS_POD_STOPPED);
				dbImage.setImageStatusDetail(Constants.DETAIL_SHUTDOWN);
				imageMapper.updateImageStatus(image, dbImage.getImageStatus(), dbImage.getImageStatusDetail());
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new Exception(Constants.E50000);
			}
		}
		
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());

		return res;
	}

	public ServpotImageStatusRes updateImageStatus(ServpotImageStatusUpdateReq imageReq) throws Exception {
		log.info("ImageService : updateImageStatus");
		DbImage dbImage = new DbImage();
		dbImage = imageMapper.selectImageByimage(imageReq.getImageId());
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		String container = imageMapper.selectContainerIdByImage(imageReq.getImageId());

		ServpotImageStatusRes res = new ServpotImageStatusRes();
		res.setImageId(imageReq.getImageId());
		
		if (container != null) {
			if (imageReq.getImageStatus().equals(Constants.STATUS_RUN) == true) {
				// 이미지 상태 변경
				imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_POD_DEPLOYING, Constants.DETAIL_DEPLOYING);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entity = new HttpEntity<>(headers);
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
						log.info("container deploy success.\nenvName : " + cmanRes.getBody().getEnvName());
						dbImage.setImageStatus(Constants.STATUS_POD_DEPLOYING);
						dbImage.setImageStatusDetail(Constants.DETAIL_DEPLOYING);
					}
				} catch (HttpClientErrorException e) {
					if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
						ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
						String status = errRes.getStatus_code().equals(Constants.DETAIL_ERROR_LOAD)? 
																		Constants.DETAIL_ERROR_LOAD: 
																		Constants.DETAIL_ERROR_PUSH; 
						imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_IMAGE_UPLOAD_FAILED, status);
					} else {
						imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
					}
					throw new Exception(Constants.E50002);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
					throw new Exception(Constants.E50000);
				}
			} else if (imageReq.getImageStatus().equals(Constants.STATUS_SUSPEND) == true) {
				// 이미지 상태 변경
				imageMapper.updateImageStatus(imageReq.getImageId(), Constants.STATUS_POD_TERMINATING, Constants.DETAIL_TERMINATING);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
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
						log.info("container recycle success.\nmessage : " + cmanRes.getBody().getMessage());
						dbImage.setImageStatus(Constants.STATUS_POD_TERMINATING);
						dbImage.setImageStatusDetail(Constants.DETAIL_TERMINATING);
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
			}
		}
		
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());
		
		return res;
	}
	
	public ServpotImageRegistRes createImageRegist(ServpotImageRegistReq imageReq) throws Exception {
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
		
		// 비동기 호출 
		CompletableFuture.runAsync(() -> {
			try {
				asyncImageRegist(imageId, imageReq);
			} catch (JsonMappingException e) {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_LOAD);
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_LOAD);
				e.printStackTrace();
			} catch (Exception e) {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_LOAD);
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
		DbImage dbImage = new DbImage();
		dbImage = imageMapper.selectImageByimage(imageReq.getImageId());
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		String container = imageMapper.selectContainerIdByImage(imageReq.getImageId());
		
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		res.setImageId(imageReq.getImageId());
		res.setImageName(imageReq.getImageName());
		
		CmanContainerUpdateReq cmanContainerReq = new CmanContainerUpdateReq();
		cmanContainerReq.setEnv(new ArrayList<CmanContainerUpdateReqEnv>());
		cmanContainerReq.setServicePort(new ArrayList<CmanContainerUpdateReqPort>());
		cmanContainerReq.setVolumeMounts(new ArrayList<CmanContainerUpdateReqVol>());
		cmanContainerReq.setProjectName(service);
		
		List<ServpotImageRegistUpdateReqSub> setupList = new ArrayList<>();
		setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_OPEN)).collect(Collectors.toList());
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
		
		HttpEntity<CmanContainerUpdateReq> containerEntity = new HttpEntity<>(cmanContainerReq, headers);
		URI uri = UriComponentsBuilder
			    .fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_UPDATE_URL)
			    .encode()
			    .buildAndExpand(container)
			    .toUri();
		
		try {
			ResponseEntity<CmanContainerUpdateRes> cmanContainerRes = restTemplate.exchange(	uri, 
																								HttpMethod.POST, 
																								containerEntity, 
																								CmanContainerUpdateRes.class);
			if (cmanContainerRes.getStatusCode() == HttpStatus.OK) {
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

		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());

		return res;
	}

	public ServpotImageDeleteRes deleteImage(ServpotImageDeleteReq imageReq) {
		log.info("ImageService : deleteImage");
		// TODO: 
		// TODO: 검토 필요
		return null;
	}

	public String asyncImageRegist(String imageId, ServpotImageRegistReq imageReq) throws Exception {
		log.info("ImageService : asyncImageRegist");
		String service = imageMapper.selectServiceByMicro(imageReq.getMicroId());
		
		CmanImageRegistReq cmanImageReq = new CmanImageRegistReq();
		cmanImageReq.setProjectName(service);
		cmanImageReq.setFileName(imageReq.getImagePath());
		cmanImageReq.setRepository(imageReq.getMicroId());
		cmanImageReq.setTag(imageId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<CmanImageRegistReq> imageEntity = new HttpEntity<>(cmanImageReq, headers);
		URI uri = UriComponentsBuilder
				    .fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_IMAGE_CREATE_URL)
				    .queryParam("remove_temp", "{remove_temp}")
				    .encode()
				    .buildAndExpand("false")
				    .toUri();

		try {
			ResponseEntity<CmanImageRegistRes> cmanImageRes = restTemplate.exchange(uri, 
																					HttpMethod.POST, 
																					imageEntity, 
																					CmanImageRegistRes.class);
			
			if (cmanImageRes.getStatusCode() == HttpStatus.OK) {
				log.info("imagename : " + cmanImageRes.getBody().getImage_name());
				
				// 컨테이너ID 생성
				String containerId = Constants.PREFIX_CON + HashUtil.makeCRC32(imageMapper.getContainerSequence());
				String[] pushCommand = cmanImageRes.getBody().getPush_command().split(Constants.SPACE);
				String imageName = pushCommand[2];

				// 이미지(harbor) 변경
				imageMapper.updateImageRealName(imageId, imageName);

				CmanContainerCreateReq cmanContainerReq = new CmanContainerCreateReq();
				cmanContainerReq.setEnv(new ArrayList<CmanContainerCreateReqEnv>());
				cmanContainerReq.setServicePort(new ArrayList<CmanContainerCreateReqPort>());
				cmanContainerReq.setVolumeMounts(new ArrayList<CmanContainerCreateReqVol>());
				cmanContainerReq.setProjectName(service);
				cmanContainerReq.setConName(containerId);
				cmanContainerReq.setImage(imageName);
				
				List<ServpotImageRegistReqSub> setupList = new ArrayList<>();
				setupList = imageReq.getSetupList().stream().filter(set -> set.getSetupType().equals(Constants.TYPE_OPEN)).collect(Collectors.toList());
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
						container.setContainerDomain(innerDomain);
						containerList.add(container);
					}
					imageMapper.insertContainer(containerList);
					imageMapper.updateImageStatus(imageId, Constants.STATUS_POD_STOPPED, Constants.DETAIL_SHUTDOWN);
				}
			}
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
				ErrorResponse errRes = ResponseUtil.parseJsonString(e.getResponseBodyAsString());
				String status = errRes.getMsg().equals(Constants.DETAIL_ERROR_LOAD)? Constants.DETAIL_ERROR_LOAD: Constants.DETAIL_ERROR_PUSH; 
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, status);
			} else {
				imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			imageMapper.updateImageStatus(imageId, Constants.STATUS_IMAGE_UPLOAD_FAILED, Constants.DETAIL_ERROR_PUSH);
			return null;
		}

		return null;
	}

}
