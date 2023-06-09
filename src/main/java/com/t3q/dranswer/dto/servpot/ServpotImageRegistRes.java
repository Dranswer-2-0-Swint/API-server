package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageRegistRes extends ApiResponse {

	private String requestId;				// 요청ID
	private String imageId;					// 이미지ID
	private String imageName;				// 이미지명
	private String imageStatus;				// 이미지상태
	private String imageStatusDetail;		// 이미지상태상세

}
