package com.t3q.dranswer.service;

import org.springframework.stereotype.Service;

import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageSetCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotImageSetCreateUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageSetUpdateRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateRes;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageService {

	public ServpotImageSetCreateRes createImageInfo(ServpotImageSetCreateUpdateReq imageReq) {
		log.info("ImageService : createMicroService");
		// TODO: DB 데이터 추가
		return null;
	}

	public ServpotImageSetUpdateRes updateImageInfo(ServpotImageSetCreateUpdateReq imageReq) {
		log.info("ImageService : updateImageInfo");
		// TODO: 컨테이너가 존재하는 경우 cman에게 컨테이너 수정 요청 
		// TODO: DB 데이터 변경
		return null;
	}

	public ServpotImageRegistRes createImageRegist(ServpotImageRegistReq imageReq, String imageId) {
		log.info("ImageService : createImageRegist");
		// TODO: cman으로 이미지 등록 요청
		// TODO: 검토 필요
		return null;
	}

	public ServpotImageStatusReadRes readImageStatus(ServpotImageStatusReadReq imageReq, String imageId) {
		log.info("ImageService : readImageStatus");
		// TODO: cman으로 이미지 상태 조회 요청
		// TODO: DB 이미지 상태 저장
		return null;
	}

	public ServpotImageStatusUpdateRes updateImageStatus(ServpotImageStatusUpdateReq imageReq, String imageId) {
		log.info("ImageService : updateImageStatus");
		// TODO: cman으로 이미지 상태 변경 요청
		// TODO: DB 이미지 상태 변경
		return null;
	}

}
