package com.t3q.dranswer.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceListReadReq;
import com.t3q.dranswer.dto.servpot.ServpotAppServiceListReadRes;
import com.t3q.dranswer.service.AppService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/api/v1/service")
public class AppServiceController {

	@Autowired
	AppService appService;
	
	// 응용서비스 목록 조회
	@GetMapping("/list")
	public ResponseEntity<Object> readAppServiceList(HttpServletRequest request, 
													@RequestBody @Valid final ServpotAppServiceListReadReq microReq) {
		
		ServpotAppServiceListReadRes res = new ServpotAppServiceListReadRes();
		
		try {
			res = appService.readAppServiceList(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 응용서비스 생성
	@PostMapping()
	public ResponseEntity<Object> createAppService(HttpServletRequest request, 
													@RequestBody @Valid final ServpotAppServiceCreateReq microReq) {
		
		ServpotAppServiceCreateRes res = new ServpotAppServiceCreateRes();
		
		try {
			res = appService.createAppService(microReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	// 응용서비스 삭제
	@DeleteMapping()
	public ResponseEntity<Object> deleteAppService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotAppServiceDeleteReq microReq) {
		
		ServpotAppServiceDeleteRes res = new ServpotAppServiceDeleteRes();
		
		try {
			res = appService.deleteAppService(microReq);
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
