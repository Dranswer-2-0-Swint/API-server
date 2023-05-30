package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerListReadRes {

	private List<CmanContainerListReadResSub> applications;		// 컨테이너 목록
	private int total;											// 컨테이너 수
	
}
