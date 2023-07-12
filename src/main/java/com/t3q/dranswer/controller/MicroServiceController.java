package com.t3q.dranswer.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.t3q.dranswer.aop.annotation.SwintValid;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.dto.servpot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.service.MicroService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Validated
@Controller
@RequestMapping("/api/v1/micro-service")
public class MicroServiceController {

	@Autowired
	MicroService microService;

	// 마이크로서비스 목록 조회
	@GetMapping("/list")
	@SwintValid
	public ResponseEntity<Object> readMicroServiceList(HttpServletRequest request, 
														@RequestParam(required = false) HashMap<String, Object> parameter) {
		
		ServpotMicroServiceListReadRes res;

		String service = (String) parameter.get("service_id");
		if (service == null || service.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
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
	@SwintValid
	public ResponseEntity<Object> createMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceCreateReq microReq) {
		
		ServpotMicroServiceCreateRes res;

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
	@SwintValid
	public ResponseEntity<Object> updateMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceUpdateReq microReq) {
		
		ServpotMicroServiceUpdateRes res;

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
	@SwintValid
	public ResponseEntity<Object> deleteMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDeleteReq microReq) {
		
		ServpotMicroServiceDeleteRes res;

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
	@SwintValid
	public ResponseEntity<Object> createMicroServiceDomain(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDomainCreateReq microReq) {
		
		ServpotMicroServiceDomainCreateRes res;

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

	@PutMapping("/domain")
	@SwintValid
	public ResponseEntity<Object> updateMicroServiceDomain(HttpServletRequest request,
														   @RequestBody @Valid final ServpotMicroServiceDomainUpdateReq microReq){

		ServpotMicroServiceDomainUpdateRes res;

		try {
			res = microService.updateMicroServiceDomain(microReq);
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
	@SwintValid
	public ResponseEntity<Object> deleteMicroServiceDomain(HttpServletRequest request, 
															@RequestBody @Valid final ServpotMicroServiceDomainDeleteReq microReq) {
		
		ServpotMicroServiceDomainDeleteRes res;

		try {
			res = microService.deleteMicroServiceDomain(microReq);
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
