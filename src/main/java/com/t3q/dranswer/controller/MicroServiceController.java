package com.t3q.dranswer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t3q.dranswer.common.util.ResponseUtil;
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
import com.t3q.dranswer.service.MicroService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/v1/micro-service")
public class MicroServiceController {

	@Autowired
	MicroService microService;
	
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
	
	@DeleteMapping()
	public ResponseEntity<Object> deleteMicroService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDeleteReq microReq) {
		
		ServpotMicroServiceDeleteRes res = new ServpotMicroServiceDeleteRes();
		
		try {
			res = microService.deleteMicroService(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/{micro_id}/domain")
	public ResponseEntity<Object> createMicroServiceDomain(HttpServletRequest request, 
														@RequestBody @Valid final ServpotMicroServiceDomainMergeReq microReq, 
														@PathVariable("micro_id") final String microId) {
		
		ServpotMicroServiceDomainMergeRes res = new ServpotMicroServiceDomainMergeRes();
		
		try {
			res = microService.createMicroServiceDomain(microReq, microId);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{micro_id}/domain")
	public ResponseEntity<Object> deleteMicroServiceDomain(HttpServletRequest request, 
															@RequestBody @Valid final ServpotMicroServiceDomainDeleteReq microReq, 
															@PathVariable("micro_id") final String microId) {
		
		ServpotMicroServiceDomainDeleteRes res = new ServpotMicroServiceDomainDeleteRes();
		
		try {
			res = microService.deleteMicroServiceDomain(microReq, microId);
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
