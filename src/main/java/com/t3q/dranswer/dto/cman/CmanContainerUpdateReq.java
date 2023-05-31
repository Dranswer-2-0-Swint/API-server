package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerUpdateReq {

	private String projectName;										// 프로젝트명
	//private String image;											// 이미지명 "harbor-dranswer.com/t3q/tomcat:stable"
	private List<CmanContainerUpdateReqEnv> env;					// 환경변수
	private List<CmanContainerUpdateReqVol> volumeMounts;			// 컨테이너 볼륨연결
	private List<CmanContainerUpdateReqPort> servicePort;			// 컨테이너 포트지정
	
}
