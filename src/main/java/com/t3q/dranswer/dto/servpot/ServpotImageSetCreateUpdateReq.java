package com.t3q.dranswer.dto.servpot;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageSetCreateUpdateReq {

	@NotNull
	private String microId;			// 마이크로서비스ID
	@NotNull
	private String imageName;		// 이미지명
	
	private ServpotImageSetCreateUpdateReqSet setList;			// 설정목록
}
