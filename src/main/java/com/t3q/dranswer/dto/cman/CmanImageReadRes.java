package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanImageReadRes {

	private String pushTime;					// docker image를 push한 시각
	private String pullTime;					// docker image를 최근에 사용한 시각
	private int size;							// docker image가 사용중인 용량
	private List<CmanImageReadResSub> tags;		// docker image에 있는 tags
	private String digest;						// docker image를 가리키는 고유 id값
	private Object config;						// docker image의 config
	
}
