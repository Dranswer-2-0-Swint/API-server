package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerDomainCreateReq {

	private String domainName;				// 도메인명
	private int port;						// 포트
	
}
