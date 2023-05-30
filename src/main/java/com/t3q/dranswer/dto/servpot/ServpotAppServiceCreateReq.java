package com.t3q.dranswer.dto.servpot;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceCreateReq {

	@NotNull
	private String companyId;			// 기업ID
	@NotNull
	private String serviceName;			// 응용서비스명

}
