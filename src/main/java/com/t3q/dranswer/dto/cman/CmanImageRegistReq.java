package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanImageRegistReq {

	private String projectName;			// 프로젝트명
	private String fileName;			// 파일이름(경로)
	private String repository;			// 레파지토리명
	private String tag;					// 테그명
	
}
