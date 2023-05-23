package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerCreateReq {

	private String projectName;								// 프로젝트명
	private String conName;									// 컨테이너명
	private String image;									// 이미지명 "harbor-dranswer.com/t3q/tomcat:stable"
	private CmanContainerCreateReqEnv env;					// 환경변수
	private CmanContainerCreateReqVol volumeMounts;			// 컨테이너 볼륨연결
	private CmanContainerCreateReqPort servicePort;			// 컨테이너 포트지정
	
}
