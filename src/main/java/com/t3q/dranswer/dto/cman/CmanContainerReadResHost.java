package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerReadResHost {

	private List<CmanContainerReadResVol> hostPath;		// 컨테이너 볼륨연결
	
}
