package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceListReadRes extends ApiResponse {

	private String requestId;									// 요청ID
	private String serviceId;									// 응용서비스ID
	private ServpotMicroServiceListReadResSub microList;		// 마이크로서비스목록

}
