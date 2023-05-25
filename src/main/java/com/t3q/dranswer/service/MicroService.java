package com.t3q.dranswer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceListReadReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateRes;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MicroService {
	
	private final RestTemplate restTemplate;

    @Autowired
    public MicroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
	public ServpotMicroServiceListReadRes readMicroServiceList(ServpotMicroServiceListReadReq microReq) {
		log.info("MicroService : readMicroServiceList");
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotMicroServiceCreateRes createMicroService(ServpotMicroServiceCreateReq microReq) {
		log.info("MicroService : createMicroService");
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotMicroServiceUpdateRes updateMicroService(ServpotMicroServiceUpdateReq microReq) {
		log.info("MicroService : updateMicroService");
		// TODO: DB 데이터 변경
		return null;
	}
	
	public ServpotMicroServiceDeleteRes deleteMicroService(ServpotMicroServiceDeleteReq microReq) {
		log.info("MicroService : deleteMicroService");
		// TODO: 마이크로서비스에 등록된 이미지 리스트 삭제 요청
		// TODO: DB 데이터 삭제
		return null;
	}
	
	public ServpotMicroServiceDomainMergeRes createMicroServiceDomain(ServpotMicroServiceDomainMergeReq microReq) {
		log.info("MicroService : createMicroServiceDomain");
		// TODO: 마이크로서비스 도메인이 있으면 삭제 후 생성
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotMicroServiceDomainDeleteRes deleteMicroServiceDomain(ServpotMicroServiceDomainDeleteReq microReq) {
		log.info("MicroService : deleteMicroServiceDomain");
		// TODO: 마이크로서비스 도메인 삭제 요청
		// TODO: DB 데이터 삭제
		return null;
	}

}
