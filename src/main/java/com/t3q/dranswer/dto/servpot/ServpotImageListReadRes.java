package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageListReadRes extends ApiResponse {

	private String requestId;								// 요청ID
	private String microId;									// 마이크로서비스ID
	private ServpotImageListReadResSub imageList;			// 이미지목록
	
}
