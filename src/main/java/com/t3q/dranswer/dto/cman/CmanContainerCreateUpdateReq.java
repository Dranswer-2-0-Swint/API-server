package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerCreateUpdateReq {

	private String projectName;								// 프로젝트명
	private String conName;									// 프로젝트명
	private String image;									// 이미지명 "harbor-dranswer.com/t3q/tomcat:stable"
	private CmanContainerCreateUpdateReqEnv env;					// 환경변수
	private CmanContainerCreateUpdateReqVol volumeMounts;			// 컨테이너 볼륨연결
	private CmanContainerCreateUpdateReqPort servicePort;			// 컨테이너 포트지정
	
}
