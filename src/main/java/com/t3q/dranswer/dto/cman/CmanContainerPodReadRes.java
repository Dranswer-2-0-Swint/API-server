package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerPodReadRes {

	private List<CmanContainerPodReadResSub> podList;		// 컨테이너 생성 시기
	private int total;										// 컨테이너 최근 배포가 진행된 시간의 합
	
}
