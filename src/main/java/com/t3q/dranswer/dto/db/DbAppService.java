package com.t3q.dranswer.dto.db;

import lombok.Data;

@Data
public class DbAppService {

	private String service;				// 응용서비스ID
	private String company;				// 기업ID
	private String serviceName;			// 응용서비스명
	private String modTimestamp;		// 변경일시
	private String regTimestamp;		// 등록일시
	
}
