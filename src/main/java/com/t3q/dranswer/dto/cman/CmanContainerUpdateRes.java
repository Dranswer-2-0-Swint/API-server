package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerUpdateRes {

	private String conName;					// 컨테이너명
	private String alias;					// 컨테이너 별칭
	private String createTime;				// 컨테이너 생성일시
	private String description;				// 설명
	private int replicas;					// 컨테이너 복제수
	private List<String> innerDomain;		// 내부 도메인 (컨테이너간 통신에 사용)
	
}
