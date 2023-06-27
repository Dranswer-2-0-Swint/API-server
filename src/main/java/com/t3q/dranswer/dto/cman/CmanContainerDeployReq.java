package com.t3q.dranswer.dto.cman;

import lombok.Data;

import java.util.List;

@Data
public class CmanContainerDeployReq {
	private boolean	 has_domain;							// 마이크로서비스 도메인 여부
	private List<CmanContainerDeployReqSub> domains; 		// 마이크로서비스 도메인 목록
}
