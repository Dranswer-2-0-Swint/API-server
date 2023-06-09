package com.t3q.dranswer.dto.cman;

import java.util.List;

import lombok.Data;

@Data
public class CmanContainerReadRes {

	private String name;									// 컨테이너 이름
	private String alias;									// 컨테이너 별칭
	private String description;								// 헬름으로 생성되었는지 아닌지를 판별하는 변수
	private List<String> envBindings;						// 컨테이너가 배포될 kubevela env, 사용되지 않음
	private String createTime;								// 컨테이너 생성 시각
	private String updateTime;								// 컨테이너 최근 변경 시각
	private String labels;									// 컨테이너에 적용된 라벨, 현재 무조건 {}이 반환됨, 사용되지 않음
	private String image;									// 컨테이너에 적용된 이미지 이름
	private String imagePullPolicy;							// 컨테이너에 적용된 image pull 정책
	private String memory;									// 컨테이너에 할당된 리소스
	private String cpu;										// 컨테이너에 할당된 리소스
	private String exposeType;								// 컨테이너 노출 방식, ClusterIP만 사용될 예정
	private List<CmanContainerReadResEnv> env;				// 환경변수
	private CmanContainerReadResHost volumeMounts;			// 컨테이너 볼륨연결
	private List<CmanContainerReadResPort> servicePort;		// 컨테이너 포트지정
	private int replicas;									// 컨테이너에 설정된 replicas 값
	private List<String> innerDomain;						// 현재 컨테이너에 연결하기 위한 도메인 경로
	private String domain;									// 현재 컨테이너가 외부로 노출되는 domain을 가지고 있을 경우 그 url를 보여줌
	
}
