package com.t3q.dranswer.dto.cman;

import lombok.Data;

import java.util.List;

@Data
public class CmanContainerDeployReq {
	private String projectName;								// 프로젝트명

	private boolean	 hasDomain;								// 마이크로서비스 도메인 여부
	private List<CmanContainerDomainSub> domainList; 		// 마이크로서비스 도메인 목록
}
