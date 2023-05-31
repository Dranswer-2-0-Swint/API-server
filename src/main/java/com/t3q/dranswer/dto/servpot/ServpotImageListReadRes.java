package com.t3q.dranswer.dto.servpot;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageListReadRes extends ApiResponse {

	private String requestId;									// 요청ID
	private String microId;										// 마이크로서비스ID
	private List<ServpotImageListReadResSub> imageList;			// 이미지목록
	
}
