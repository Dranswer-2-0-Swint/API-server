package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerReadResVol {

	private String name;				// 볼륨명
	private String path;				// 외부경로
	private String mountPath;			// 내부경로
	private String subPath;				// 서브경로
	
}
