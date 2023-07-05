package com.t3q.dranswer.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.t3q.dranswer.aop.annotation.SwintValid;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.RequestContext;
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
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateReq;
import com.t3q.dranswer.service.ImageService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/api/v1/image")
public class ImageController {

	@Autowired
	ImageService imageService;

	// 이미지 목록 조회
	@GetMapping("/list")
	@SwintValid
	public ResponseEntity<Object> readImageList(HttpServletRequest request, 
													@RequestParam(required = false) HashMap<String, Object> parameter) {
		
		ServpotImageListReadRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		String micro = (String) parameter.get("micro_id");
		if (micro == null || micro.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			res = imageService.readImageList(micro);
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
	
	// 이미지 조회
	@GetMapping()
	@SwintValid
	public ResponseEntity<Object> readImage(HttpServletRequest request, 
													@RequestParam(required = false) HashMap<String, Object> parameter) {
		
		ServpotImageReadRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		String image = (String) parameter.get("image_id");
		if (image == null || image.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			res = imageService.readImage(image);
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
	
	// 이미지 상태 조회
	@GetMapping("/status")
	@SwintValid
	public ResponseEntity<Object> readImageStatus(HttpServletRequest request, 
													@RequestParam(required = false) HashMap<String, Object> parameter) {
		
		ServpotImageStatusRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		String image = (String) parameter.get("image_id");
		if (image == null || image.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			res = imageService.readImageStatus(image);
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
	
	// 이미지 상태 변경
	@PutMapping("/status")
	@SwintValid
	public ResponseEntity<Object> updateImageStatus(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageStatusUpdateReq imageReq) {
		
		ServpotImageStatusRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = imageService.updateImageStatus(imageReq);
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
	
	// 이미지 등록
	@PostMapping("/regist")
	@SwintValid
	public ResponseEntity<Object> createImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistReq imageReq) {
		
		ServpotImageRegistRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = imageService.createImageRegist(imageReq);
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
	
	// 이미지 등록 변경
	@PutMapping("/regist")
	@SwintValid
	public ResponseEntity<Object> updateImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistUpdateReq imageReq) {
		
		ServpotImageRegistRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = imageService.updateImageRegist(imageReq);
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
	
	// 이미지 삭제
	@DeleteMapping()
	@SwintValid
	public ResponseEntity<Object> deleteImage(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageDeleteReq imageReq) {
		
		ServpotImageDeleteRes res;
		RequestContext.RequestContextData localdata = RequestContext.getContextData();

		try {
			res = imageService.deleteImage(imageReq.getImageId());
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
