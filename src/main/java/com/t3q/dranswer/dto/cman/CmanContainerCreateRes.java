package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerCreateRes {

	private String conName;					// 컨테이너명
	private String alias;					// 컨테이너 별칭
	private String createTime;				// 컨테이너 생성일시
	private int replicas;					// 컨테이너 복제수
	private String innerDomain;				// 내부 도메인 (컨테이너간 통신에 사용)
	
}
