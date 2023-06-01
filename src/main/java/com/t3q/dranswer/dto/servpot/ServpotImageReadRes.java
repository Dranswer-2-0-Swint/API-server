package com.t3q.dranswer.dto.servpot;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotImageReadRes extends ApiResponse {

	private String requestId;				// 요청ID
	private String microId;					// 마이크로서비스ID
	private String imageId;					// 이미지ID
	private String imageName;				// 이미지명
	private String registTime;				// 최초등록시간
	private String modifyTime;				// 최종수정시간
	private String startTime;				// 최종실행시간
	private String stopTime;				// 최종종료시간
	private String runningTime;				// 실행시간
	private List<ServpotImageReadResSub> imageDomainList;		// 이미지도메인

}
