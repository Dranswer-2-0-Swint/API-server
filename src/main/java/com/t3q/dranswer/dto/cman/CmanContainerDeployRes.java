package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerDeployRes {

	private String createTime;				// 구동일시
	private String envName;					// env명
	private String version;					// 리비전 버전
	
}
