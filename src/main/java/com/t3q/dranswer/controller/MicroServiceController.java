package com.t3q.dranswer.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateRes;
import com.t3q.dranswer.service.MicroService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/api/v1/micro-service")
public class MicroServiceController {

	@Autowired
	MicroService microService;
	
	// 마이크로서비스 목록 조회
	@GetMapping("/list")
	public ResponseEntity<Object> readMicroServiceList(HttpServletRequest request, 
														@RequestParam(required = false) HashMap<String, Object> parameter) {
		
		ServpotMicroServiceListReadRes res = new ServpotMicroServiceListReadRes();
		
		String service = (String) parameter.get("service_id");
		if (service == null || service.isEmpty() == true) {
			// exception
		}
		
		try {
			res = microService.readMicroServiceList(service);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 마이크로서비스 생성
	@PostMapping()
	public ResponseEntity<Object> createMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceCreateReq microReq) {
		
		ServpotMicroServiceCreateRes res = new ServpotMicroServiceCreateRes();
		
		try {
			res = microService.createMicroService(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 마이크로서비스 변경
	@PutMapping()
	public ResponseEntity<Object> updateMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceUpdateReq microReq) {
		
		ServpotMicroServiceUpdateRes res = new ServpotMicroServiceUpdateRes();
		
		try {
			res = microService.updateMicroService(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 마이크로서비스 삭제
	@DeleteMapping()
	public ResponseEntity<Object> deleteMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDeleteReq microReq) {
		
		ServpotMicroServiceDeleteRes res = new ServpotMicroServiceDeleteRes();
		
		try {
			res = microService.deleteMicroService(microReq.getMicroId());
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 마이크로서비스 도메인 설정
	@PostMapping("/domain")
	public ResponseEntity<Object> createMicroServiceDomain(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDomainMergeReq microReq) {
		
		ServpotMicroServiceDomainMergeRes res = new ServpotMicroServiceDomainMergeRes();
		
		try {
			res = microService.createMicroServiceDomain(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 마이크로서비스 도메인 삭제
	@DeleteMapping("/domain")
	public ResponseEntity<Object> deleteMicroServiceDomain(HttpServletRequest request, 
															@RequestBody @Valid final ServpotMicroServiceDomainDeleteReq microReq) {
		
		ServpotMicroServiceDomainDeleteRes res = new ServpotMicroServiceDomainDeleteRes();
		
		try {
			res = microService.deleteMicroServiceDomain(microReq.getMicroId());
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
}
