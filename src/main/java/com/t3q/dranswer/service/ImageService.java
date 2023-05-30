package com.t3q.dranswer.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReq;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqEnv;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqPort;
import com.t3q.dranswer.dto.cman.CmanContainerCreateReqVol;
import com.t3q.dranswer.dto.cman.CmanContainerCreateRes;
import com.t3q.dranswer.dto.cman.CmanContainerPodReadRes;
import com.t3q.dranswer.dto.cman.CmanContainerTimeReadRes;
import com.t3q.dranswer.dto.cman.CmanImageRegistReq;
import com.t3q.dranswer.dto.cman.CmanImageRegistRes;
import com.t3q.dranswer.dto.db.DbImage;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadResSub;
import com.t3q.dranswer.dto.servpot.ServpotImageReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReqSub;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReq;
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
	
//	public ServpotImageListReadRes readImageList(String micro) {
//		log.info("ImageService : readImageList");
//		String service = imageMapper.selectServiceByMicro(micro);
//		
//		List<DbImage> dbImageList = new ArrayList<>();
//		dbImageList = imageMapper.selectImageByMicro(micro);
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<String> entity = new HttpEntity<>(headers);
//		URI uri = UriComponentsBuilder
//			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_STATUS_READ_URL)
//			    	.queryParam("projectName", "{projectName}")
//			    	.encode()
//			    	.buildAndExpand(service)
//			    	.toUri();
//		try {
//			ResponseEntity<List<CmanContainerStatusListReadRes>> cmanRes = restTemplate.exchange(
//																			uri, 
//																			HttpMethod.GET, 
//																			entity, 
//																			new ParameterizedTypeReference<List<CmanContainerStatusListReadRes>>() {});
//			if (cmanRes.getStatusCode() == HttpStatus.OK) {
//				List<CmanContainerStatusListReadRes> conStatusList = cmanRes.getBody();
//				dbImageList.stream()
//					.forEach(dbImage -> conStatusList.stream()
//						.filter(conStatus -> conStatus.getContainer().equals(dbImage.getContainer()))
//						.findFirst()
//						.ifPresent(conStatus -> dbImage.setImageStatusDetail(ResponseUtil.getStatusCode(conStatus.getState())))
//					);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.getMessage());
//			return null;
//		}
//		
//		ServpotImageListReadRes res = new ServpotImageListReadRes();
//		res.setImageList(new ArrayList<ServpotImageListReadResSub>());
//		res.setMicroId(micro);
//		for (DbImage dbImage : dbImageList) {
//			ServpotImageListReadResSub sub = new ServpotImageListReadResSub();
//			sub.setImageId(dbImage.getImage());
//			sub.setImageName(dbImage.getImageName());
//			res.getImageList().add(sub);
//		}
//
//		return res;
//	}

	public ServpotImageReadRes readImage(String image) {
		log.info("ImageService : readImage");
		ServpotImageReadRes res = new ServpotImageReadRes();
		DbImage dbImage = new DbImage();
		dbImage = imageMapper.selectImageByimage(image);
		
		if (dbImage == null) {
			// exception
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_TIME_READ_URL)
			    	.encode()
			    	.buildAndExpand(dbImage.getContainer())
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
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		res.setMicroId(dbImage.getMicroService());
		res.setImageId(dbImage.getImage());
		res.setImageName(dbImage.getImageName());
		res.setImageDomain(dbImage.getContainerDomain());
		
		return null;
	}

	public ServpotImageStatusRes readImageStatus(String image) {
		log.info("ImageService : readImageStatus");
		DbImage dbImage = new DbImage();
		dbImage = imageMapper.selectImageByimage(image);
		
		String service = imageMapper.selectServiceByMicro(dbImage.getMicroService());
		
		if (dbImage.getImageStatus() != Constants.STATUS_POD_STOPPED && 
				dbImage.getImageStatus() != Constants.STATUS_DEPLOY_FAILED &&
				dbImage.getImageStatus() != Constants.STATUS_IMAGE_UPLOAD_FAILED) {
			
			if (dbImage.getContainer() == null) {
				// exception
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			URI uri = UriComponentsBuilder
				    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_POD_READ_URL)
					    .queryParam("projectName", "{projectName}")
					    .encode()
					    .buildAndExpand(dbImage.getContainer(), service)
					    .toUri();
			try {
				ResponseEntity<CmanContainerPodReadRes> cmanRes = restTemplate.exchange(uri, 
																						HttpMethod.GET, 
																						entity, 
																						CmanContainerPodReadRes.class);
				if (cmanRes.getStatusCode() == HttpStatus.OK) {
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return null;
			}
		}
		
		ServpotImageStatusRes res = new ServpotImageStatusRes();
		res.setImageId(image);
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());
		
		return res;
	}

	public ServpotImageStatusRes updateImageStatus(ServpotImageStatusUpdateReq imageReq) {
		log.info("ImageService : updateImageStatus");
		// TODO: cman으로 이미지 상태 변경 요청
		// TODO: DB 이미지 상태 변경
		return null;
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
		
		if (imageMapper.insertImage(dbImage) == 0) {
			// exception
		}
		
		// 비동기 호출 
		CompletableFuture.runAsync(() -> asyncImageRegist(imageId, imageReq));

		// 응답 생성
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		res.setImageId(dbImage.getImage());
		res.setImageName(dbImage.getImageName());
		res.setImageStatus(dbImage.getImageStatus());
		res.setImageStatusDetail(dbImage.getImageStatusDetail());
		
		return res;
	}

	public ServpotImageRegistRes updateImageRegist(ServpotImageRegistUpdateReq imageReq) {
		log.info("ImageService : updateImageRegist");
		// TODO: 
		// TODO: 검토 필요
		return null;
	}

	public ServpotImageDeleteRes deleteImage(ServpotImageDeleteReq imageReq) {
		log.info("ImageService : deleteImage");
		// TODO: 
		// TODO: 검토 필요
		return null;
	}

	public String asyncImageRegist(String imageId, ServpotImageRegistReq imageReq) {
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
			if (imageMapper.updateImageRealName(imageId, imageName) == 0) {
				// exception
			}

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
				if (imageMapper.updateImageContainer(imageId, containerId, cmanContainerRes.getBody().getInnerDomain().get(0)) == 0) {
					// exception
				}
			}
		}

		return null;
	}

}
