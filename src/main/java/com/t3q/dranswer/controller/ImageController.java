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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotImageDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadReq;
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
	public ResponseEntity<Object> readImageList(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageListReadReq imageReq) {
		
		ServpotImageListReadRes res = new ServpotImageListReadRes();
		
		try {
			res = imageService.readImageList(imageReq);
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
	public ResponseEntity<Object> readImage(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageReadReq imageReq) {
		
		ServpotImageReadRes res = new ServpotImageReadRes();
		
		try {
			res = imageService.readImage(imageReq);
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
	public ResponseEntity<Object> readImageStatus(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageStatusReadReq imageReq) {
		
		ServpotImageStatusRes res = new ServpotImageStatusRes();
		
		try {
			res = imageService.readImageStatus(imageReq);
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
	public ResponseEntity<Object> updateImageStatus(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageStatusUpdateReq imageReq) {
		
		ServpotImageStatusRes res = new ServpotImageStatusRes();
		
		try {
			res = imageService.updateImageStatus(imageReq);
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
	public ResponseEntity<Object> createImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistReq imageReq) {
		
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		
		try {
			res = imageService.createImageRegist(imageReq);
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
	public ResponseEntity<Object> updateImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistUpdateReq imageReq) {
		
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		
		try {
			res = imageService.updateImageRegist(imageReq);
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
	public ResponseEntity<Object> deleteImage(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageDeleteReq imageReq) {
		
		ServpotImageDeleteRes res = new ServpotImageDeleteRes();
		
		try {
			res = imageService.deleteImage(imageReq);
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
