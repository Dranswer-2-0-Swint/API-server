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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.service.AppService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/api/v1/service")

public class AppServiceController {

	@Autowired
	AppService appService;

	// 응용서비스 목록 조회
	@GetMapping("/all")
	@SwintValid
	public ResponseEntity<Object> readAppServiceAll(HttpServletRequest request) {

		ServpotAppServiceAllReadRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = appService.readAppServiceAll();
			res.setRequestId(localdata.getRequestId());
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()),
					new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	// 응용서비스 목록 조회
	@GetMapping("/list")
	@SwintValid
	public ResponseEntity<Object> readAppServiceList(HttpServletRequest request,
													 @RequestParam(required = false) HashMap<String, Object> parameter) {

		ServpotAppServiceListReadRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		String company = (String) parameter.get("company_id");
		if (company == null || company.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			res = appService.readAppServiceList(company);
			res.setRequestId(localdata.getRequestId());
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
	@SwintValid
	public ResponseEntity<Object> createAppService(HttpServletRequest request, 
													@RequestBody @Valid final ServpotAppServiceCreateReq serviceReq) {
		
		ServpotAppServiceCreateRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = appService.createAppService(serviceReq);
			res.setRequestId(localdata.getRequestId());
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
	@SwintValid
	public ResponseEntity<Object> deleteService(HttpServletRequest request, 
														@RequestBody @Valid final ServpotAppServiceDeleteReq serviceReq) {
		
		ServpotAppServiceDeleteRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = appService.deleteService(serviceReq.getServiceId());
			res.setRequestId(localdata.getRequestId());
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
