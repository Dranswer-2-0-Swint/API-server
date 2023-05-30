package com.t3q.dranswer.dto.servpot;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageRegistReqSub {

	@NotNull
	private int setupSeq;					// 설정순번
	@NotNull
	private String setupType;				// 설정타입 (포트개방/포트연결/볼륨연결/환경변수)
	private String setupKey;				// 설정키 (환경변수 설정 시 사용)
	@NotNull
	private String setupValue;				// 설정값

}
