package com.t3q.dranswer.controller;

import com.t3q.dranswer.aop.annotation.SwintValid;
import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.servpot.*;
import com.t3q.dranswer.service.AppService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Log4j2
@Validated
@Controller
@RequestMapping("/api/v1/service")
public class AppServiceController {

	@Autowired
	AppService appService;

	// 응용서비스 세부정보 조회 (하위 마이크로서비스, 이미지 조회)
	@GetMapping("/detail")
	@SwintValid
	public ResponseEntity<Object> readAllMicroAndImages(HttpServletRequest request,
														@RequestParam(required = false) HashMap<String, Object> parameter) {


		ServpotAppServiceReadMicroServicesAndImagesRes res;

		String service = (String) parameter.get("service_id");
		if (service == null || service.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
		}


		try {
			res = appService.readMicroServicesAndImages(service);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()),
					new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}


	// 전체 응용서비스 목록 조회
	@GetMapping("/all")
	@SwintValid
	public ResponseEntity<Object> readAppServiceAll(HttpServletRequest request) {

		ServpotAppServiceAllReadRes res;

		try {
			res = appService.readAppServiceAll();
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

		String company = (String) parameter.get("company_id");
		if (company == null || company.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
		}

		try {
			res = appService.readAppServiceList(company);
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

		try {
			res = appService.createAppService(serviceReq);
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

		try {
			res = appService.deleteService(serviceReq.getServiceId());
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
