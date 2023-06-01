package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class ErrorResponse {

	private String status_code;				// 응답코드
	private String msg;						// 응답메시지
	private String detail;					// 상세메시지
	
}
