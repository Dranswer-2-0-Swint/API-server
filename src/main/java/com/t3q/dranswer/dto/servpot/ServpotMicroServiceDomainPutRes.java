package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceDomainPutRes extends ApiResponse{
    private String requestId;			// 요청ID
    private String microId;				// 마이크로서비스ID
    private String renewMicroDomain;	// 변경할 마이크로서비스 도메인
    private  String microDomainPath;    // 마이크로서비스 도메인 패스
    private  int microDomainPort;       // 마이크로 서비스 도메인 포트
}
