package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceListReadRes extends ApiResponse {

	private String requestId;								// 요청ID
	private String companyId;								// 기업ID
	private ServpotAppServiceListReadResSub serviceList;		// 응용서비스목록

}
