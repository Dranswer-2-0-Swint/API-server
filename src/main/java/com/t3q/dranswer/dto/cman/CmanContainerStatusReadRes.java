package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerStatusReadRes {

	private String state;				// 파드상태
	private Object event;				// 파드이벤트
	
}
