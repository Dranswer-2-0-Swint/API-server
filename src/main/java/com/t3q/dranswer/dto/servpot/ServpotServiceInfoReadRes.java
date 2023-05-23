package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotServiceInfoReadRes extends ApiResponse {

	private String requestId;			// 요청ID
	private String service_id;						// 마이크로서비스ID
	private ServpotServiceInfoReadResMicro microList;		// 설정목록
}
