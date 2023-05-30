package com.t3q.dranswer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.t3q.dranswer.dto.servpot.ServpotAppServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceListReadReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceListReadRes;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AppService {
	
	private final RestTemplate restTemplate;

    @Autowired
    public AppService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
	public ServpotAppServiceListReadRes readAppServiceList(ServpotAppServiceListReadReq microReq) {
		log.info("MicroService : readMicroServiceList");
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotAppServiceCreateRes createAppService(ServpotAppServiceCreateReq microReq) {
		log.info("MicroService : createMicroService");
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotAppServiceDeleteRes deleteAppService(ServpotAppServiceDeleteReq microReq) {
		log.info("MicroService : deleteMicroService");
		// TODO: 마이크로서비스에 등록된 이미지 리스트 삭제 요청
		// TODO: DB 데이터 삭제
		return null;
	}
	
}
