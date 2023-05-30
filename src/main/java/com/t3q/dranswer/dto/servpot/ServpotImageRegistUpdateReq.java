package com.t3q.dranswer.dto.servpot;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageRegistUpdateReq {

	@NotNull
	private String imageId;											// 이미지ID
	@NotNull
	private String imageName;										// 이미지명
	
	private List<ServpotImageRegistUpdateReqSub> setupList;			// 설정목록
}
