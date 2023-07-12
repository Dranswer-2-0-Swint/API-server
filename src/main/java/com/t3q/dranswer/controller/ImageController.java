package com.t3q.dranswer.controller;

import com.t3q.dranswer.aop.annotation.SwintValid;
import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.servpot.*;
import com.t3q.dranswer.service.ImageService;
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

		String micro = (String) parameter.get("micro_id");
		if (micro == null || micro.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
		}
		
		try {
			res = imageService.readImageList(micro);
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

		String image = (String) parameter.get("image_id");
		if (image == null || image.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
		}
		
		try {
			res = imageService.readImage(image);
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

		String image = (String) parameter.get("image_id");
		if (image == null || image.isEmpty()) {
			return new ResponseEntity<Object>(ResponseUtil.parseRspCode(Constants.E40001),
												new HttpHeaders(),
												HttpStatus.BAD_REQUEST);
		}
		
		try {
			res = imageService.readImageStatus(image);
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
	@SwintValid
	public ResponseEntity<Object> createImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistReq imageReq) {
		
		ServpotImageRegistRes res;

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
	@SwintValid
	public ResponseEntity<Object> updateImageRegist(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageRegistUpdateReq imageReq) {
		
		ServpotImageRegistRes res;

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
	@SwintValid
	public ResponseEntity<Object> deleteImage(HttpServletRequest request, 
													@RequestBody @Valid final ServpotImageDeleteReq imageReq) {
		
		ServpotImageDeleteRes res;

		try {
			res = imageService.deleteImage(imageReq.getImageId());
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
