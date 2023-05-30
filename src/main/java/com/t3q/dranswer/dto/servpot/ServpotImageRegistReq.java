package com.t3q.dranswer.dto.servpot;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageRegistReq {

	@NotNull
	private String microId;										// 마이크로서비스ID
	@NotNull
	private String imageName;									// 이미지명
	@NotNull
	private String imagePath;									// 이미지 저장경로
	private List<ServpotImageRegistReqSub> setupList;			// 설정목록
	
}
