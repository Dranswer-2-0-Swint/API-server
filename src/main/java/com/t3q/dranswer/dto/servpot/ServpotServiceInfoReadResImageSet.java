package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotServiceInfoReadResImageSet {

	private int setSeq;					// 설정순번
	private String setType;				// 설정타입 (포트개방/포트연결/볼륨연결/환경변수)
	private String setKey;				// 설정키 (환경변수 설정 시 사용)
	private String setValue;			// 설정값

}