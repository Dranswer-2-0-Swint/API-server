package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanImageRegistRes {

	private String message;				// 응답메시지
	private String image_name;			// 이미지명 "postgres:test"
	private String push_command;			// 명령어 "docker push svc-harbor-dev.com/test/test:5.09"
	
}
