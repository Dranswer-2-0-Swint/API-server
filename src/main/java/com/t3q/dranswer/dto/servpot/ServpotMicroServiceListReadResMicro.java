package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceListReadResMicro {

	private String microId;												// 마이크로서비스ID
	private String microName;											// 마이크로서비스명
	private List<ServpotMicroServiceListReadResMicroDomain> domains;			// 마이크로서비스도메인목록

}
