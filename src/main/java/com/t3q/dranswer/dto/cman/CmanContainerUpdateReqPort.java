package com.t3q.dranswer.dto.cman;

import lombok.Data;

@Data
public class CmanContainerUpdateReqPort {

	private int	port;					// 컨테이너 포트
	private String protocol;			// 컨테이너 프로토콜
	private boolean expose = true;		// 노출여부
	private int nodePort;				// 노드포트
	
}
