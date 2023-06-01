package com.t3q.dranswer.dto.db;

import lombok.Data;

@Data
public class DbContainer {

	private String image;						// 이미지ID
	private String container;					// 컨테이너ID
	private String containerDomain;				// 컨테이너도메인
	private String modTimestamp;				// 변경일시
	private String regTimestamp;				// 등록일시
	
}
