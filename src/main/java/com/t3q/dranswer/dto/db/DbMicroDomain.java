package com.t3q.dranswer.dto.db;

import lombok.Data;

@Data
public class DbMicroDomain {

    private String microService;				// 마이크로서비스ID
    private String domain;						// 마이크로서비스도메인
    private String path;                        // 마이크로서비스도메인패스
    private String port;                        // 마이크로서비스포트
    private String modTimestamp;				// 변경일시
    private String regTimestamp;				// 등록일시
}
