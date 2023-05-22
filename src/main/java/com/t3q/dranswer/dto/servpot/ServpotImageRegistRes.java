package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageRegistRes extends ApiResponse {

	private String requestId;			// 요청ID
	private String imageId;				// 이미지ID
	private String imagePath;			// 이미지 저장경로

}
