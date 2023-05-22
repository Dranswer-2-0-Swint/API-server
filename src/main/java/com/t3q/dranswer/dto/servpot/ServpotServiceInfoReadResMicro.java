package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotServiceInfoReadResMicro {

	private String microId;							// 마이크로서비스ID
	private String microName;						// 마이크로서비스명
	private String microDomain;						// 마이크로서비스도메인
	private ServpotServiceInfoReadResImage imageList;		// 설정목록
}
