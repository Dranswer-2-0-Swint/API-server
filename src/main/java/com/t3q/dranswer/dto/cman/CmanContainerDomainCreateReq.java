package com.t3q.dranswer.dto.cman;

import lombok.Data;

import java.util.List;

@Data
public class CmanContainerDomainCreateReq {

	private String domainName;				// 도메인명
	private int port;						// 포트
}

