package com.t3q.dranswer.dto.servpot;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceDomainCreateRes extends ApiResponse{

    private String requestId;			// 요청ID
    private String microId;				// 마이크로서비스ID
    private String domain;		    	// 마이크로서비스 도메인
    private String path; 		    	// 마이크로서비스 도메인 패스
    private int port;			        // 마이크로서비스 도메인 포트

}
