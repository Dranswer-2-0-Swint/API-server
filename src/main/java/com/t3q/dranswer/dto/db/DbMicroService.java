package com.t3q.dranswer.dto.db;

import com.t3q.dranswer.service.MicroService;
import lombok.Data;

@Data
public class DbMicroService {

	private String microService;				// 마이크로서비스ID
	private String service;						// 응용서비스ID
	private String microServiceName;			// 마이크로서비스명
	private String modTimestamp;				// 변경일시
	private String regTimestamp;				// 등록일시

}
