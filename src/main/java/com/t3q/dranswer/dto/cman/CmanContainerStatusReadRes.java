package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerStatusReadRes {

	private String container;				// 컨테이너명
	private String state;					// 컨테이너상태
	
}
