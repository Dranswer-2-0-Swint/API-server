package com.t3q.dranswer.service;

import org.springframework.stereotype.Service;

import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateRes;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MicroService {

	public ServpotMicroServiceCreateRes createMicroService(ServpotMicroServiceCreateReq microReq) {
		log.info("MicroService : createMicroService");
		// TODO: cman으로 마이크로서비스 생성 요청
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
		// TODO: cman으로 마이크로서비스 삭제 요청
		// TODO: DB 데이터 삭제
		return null;
	}
	
	public ServpotMicroServiceDomainMergeRes createMicroServiceDomain(ServpotMicroServiceDomainMergeReq microReq, String microId) {
		log.info("MicroService : createMicroServiceDomain");
		// TODO: cman으로 마이크로서비스 도메인 생성 요청
		// TODO: DB 데이터 추가
		return null;
	}
	
	public ServpotMicroServiceDomainDeleteRes deleteMicroServiceDomain(ServpotMicroServiceDomainDeleteReq microReq, String microId) {
		log.info("MicroService : deleteMicroServiceDomain");
		// TODO: cman으로 마이크로서비스 도메인 삭제 요청
		// TODO: DB 데이터 삭제
		return null;
	}

}
