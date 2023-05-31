package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerTimeReadRes {

	private String createTime;				// 컨테이너 생성 시기
	private String lastUpdateTime;			// 컨테이너 최근 변경 시각
	private String lastDeployTime;			// 컨테이너 최근 배포 시각
	private String lastRecycleTime;			// 컨테이너 최근 배포 취소 시각
	private String lastAge;					// 컨테이너 최근 배포 진행 시간
	private String totalAge;				// 컨테이너 최근 배포가 진행된 시간의 합
	
}
