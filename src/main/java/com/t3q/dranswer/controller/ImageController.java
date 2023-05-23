package com.t3q.dranswer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistReq;
import com.t3q.dranswer.dto.servpot.ServpotImageRegistRes;
import com.t3q.dranswer.dto.servpot.ServpotImageSetCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotImageSetCreateUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageSetUpdateRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusReadRes;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotImageStatusUpdateRes;
import com.t3q.dranswer.service.ImageService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/v1/image")
public class ImageController {
	
	@Autowired
	ImageService imageService;

	@PostMapping("/info")
	public ResponseEntity<Object> createImageInfo(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageSetCreateUpdateReq imageReq) {
		
		ServpotImageSetCreateRes res = new ServpotImageSetCreateRes();
		
		try {
			res = imageService.createImageInfo(imageReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PutMapping("/info")
	public ResponseEntity<Object> updateImageInfo(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageSetCreateUpdateReq imageReq) {
		
		ServpotImageSetUpdateRes res = new ServpotImageSetUpdateRes();
		
		try {
			res = imageService.updateImageInfo(imageReq);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/{image_id}/regist")
	public ResponseEntity<Object> createImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistReq imageReq, 
													@PathVariable("image_id") final String imageId) {
		
		ServpotImageRegistRes res = new ServpotImageRegistRes();
		
		try {
			res = imageService.createImageRegist(imageReq, imageId);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{image_id}/status")
	public ResponseEntity<Object> readImageStatus(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageStatusReadReq imageReq, 
													@PathVariable("image_id") final String imageId) {
		
		ServpotImageStatusReadRes res = new ServpotImageStatusReadRes();
		
		try {
			res = imageService.readImageStatus(imageReq, imageId);
		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(e.getMessage()), 
												new HttpHeaders(), 
												HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PutMapping("/{image_id}/status")
	public ResponseEntity<Object> updateImageStatus(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageStatusUpdateReq imageReq, 
													@PathVariable("image_id") final String imageId) {
		
		ServpotImageStatusUpdateRes res = new ServpotImageStatusUpdateRes();
		
		try {
			res = imageService.updateImageStatus(imageReq, imageId);
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
