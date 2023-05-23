package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageSetCreateRes extends ApiResponse {

	private String requestId;			// 요청ID
	private String microId;				// 마이크로서비스ID
	private String imageId;				// 이미지ID
	private String imageName;			// 이미지명

}
