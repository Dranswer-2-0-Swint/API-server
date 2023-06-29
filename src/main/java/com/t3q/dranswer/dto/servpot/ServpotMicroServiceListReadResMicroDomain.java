package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceListReadResMicroDomain {

	private String domain;				// 마이크로서비스ID
	private String path;				// 마이크로서비스명
	private int port;					// 마이크로서비스도메인

}
