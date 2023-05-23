package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerUpdateReqEnv {

	private String name;			// 환경변수명
	private String value;			// 환경변수값
	
}
