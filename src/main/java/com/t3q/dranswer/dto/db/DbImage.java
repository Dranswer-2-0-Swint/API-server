package com.t3q.dranswer.dto.db;

import lombok.Data;

@Data
public class DbImage {

	private String image;						// 이미지ID
	private String microService;				// 마이크로서비스ID
	private String imageName;					// 이미지명
	private String imageRealName;				// 이미지명(하버)
	private String imageStatus;					// 이미지상태
	private String imageStatusDetail;			// 이미지상태상세
	private String modTimestamp;				// 변경일시
	private String regTimestamp;				// 등록일시
	
}
