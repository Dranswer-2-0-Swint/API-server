package com.t3q.dranswer.dto.servpot;


import lombok.Data;

@Data
public class ServpotMicroServiceDomainPutReqSub {
    private String domain;              //마이크로서비스 도메인
    private int port;                   //마이크로서비스 포트
    private String path;                //마이크로서비스 패스
}
