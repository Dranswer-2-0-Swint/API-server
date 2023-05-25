package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceCreateRes extends ApiResponse {

	private String requestId;					// 요청ID
	private String companyId;					// 기업ID
	private String serviceId;					// 응용서비스ID
	private String serviceName;					// 응용서비스명

}
