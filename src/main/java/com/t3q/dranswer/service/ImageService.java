package com.t3q.dranswer.service;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

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

import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.CmanImageRegistReq;
import com.t3q.dranswer.dto.cman.CmanImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateReq;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageService {
	
	private final RestTemplate restTemplate;

    @Autowired
    public ImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
	public ServpotImageListReadRes readImageList(ServpotImageListReadReq imageReq) {
		log.info("ImageService : readImageList");
		// TODO: 
		// TODO: 검토 필요
		return null;
	}

	public ServpotImageReadRes readImage(ServpotImageReadReq imageReq) {
		log.info("ImageService : readImage");
		// TODO: 
		// TODO: 검토 필요
		return null;
	}

	public ServpotImageStatusRes readImageStatus(ServpotImageStatusReadReq imageReq) {
		log.info("ImageService : readImageStatus");
		// TODO: cman으로 이미지 상태 조회 요청
		// TODO: DB 이미지 상태 저장
		return null;
	}

	public ServpotImageStatusRes updateImageStatus(ServpotImageStatusUpdateReq imageReq) {
		log.info("ImageService : updateImageStatus");
		// TODO: cman으로 이미지 상태 변경 요청
		// TODO: DB 이미지 상태 변경
		return null;
	}
	
	public ServpotImageRegistRes createImageRegist(ServpotImageRegistReq imageReq) {
		log.info("ImageService : createImageRegist");
		// TODO: 
		CmanImageRegistReq cmanReq = new CmanImageRegistReq();
		CompletableFuture.runAsync(() -> asyncImageRegist(cmanReq));
		
		// TODO: 검토 필요
		return null;
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

	public String asyncImageRegist(CmanImageRegistReq cmanReq) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<CmanImageRegistReq> apiRequest = new HttpEntity<>(cmanReq, headers);
		URI uri = UriComponentsBuilder
						    .fromUriString(Constants.CMAN_IMAGE_CREATE_URL)
						    .queryParam("remove_temp", "{remove_temp}").encode().buildAndExpand("false").toUri();

		ResponseEntity<CmanImageRegistRes> apiResponse = restTemplate.exchange(uri, HttpMethod.POST, apiRequest, CmanImageRegistRes.class);
		
		if (apiResponse.getStatusCode() == HttpStatus.OK) {
		}

		return null;
	}

}
