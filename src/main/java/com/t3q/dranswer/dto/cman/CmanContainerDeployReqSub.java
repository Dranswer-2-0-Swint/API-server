package com.t3q.dranswer.dto.cman;


import lombok.Data;

@Data
public class CmanContainerDeployReqSub {
    private String domain;
    private int port;
    private String path;
}
