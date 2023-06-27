package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotMicroServiceDomainPutReq {
    @NotNull
    private String microId;				// 마이크로서비스ID
    @NotNull
    private String microDomain;			// 마이크로서비스 도메인
    @NotNull
    private  String microDomainPath;    // 마이크로서비스 도메인 패스
    @NotNull
    private  int microDomainPort;    // 마이크로 서비스 도메인 포트

}
