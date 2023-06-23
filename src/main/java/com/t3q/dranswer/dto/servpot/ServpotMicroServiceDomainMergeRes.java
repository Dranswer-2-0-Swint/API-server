package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceDomainMergeRes extends ApiResponse {

	private String requestId;			// 요청ID
	private String microId;				// 마이크로서비스ID
	private String microDomain;			// 마이크로서비스 도메인

	private String microPath; 			// 마이크로서비스 도메인 패스

	private String microPort;			// 마이크로서비스 도메인 포트

}
