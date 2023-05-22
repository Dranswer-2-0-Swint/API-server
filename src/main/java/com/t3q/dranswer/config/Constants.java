package com.t3q.dranswer.config;

public class Constants {

	public static final String ACCESS_TOKEN_NAME		= "dr_access_token";
	public static final String REFRESH_TOKEN_NAME		= "dr_refresh_token";
	
	
	// naver cloud
	public static final String KEYCLOAK_REALM 			= "service-user-dev";
	public static final String KEYCLOAK_CLIENT 			= "login";
	public static final String KEYCLOAK_SECRET 			= "JG6eaVSJVzbUp5Sgp7MAyByrraZX7xNC";
	public static final String KEYCLOAK_SERVER 			= "http://27.96.130.179:5010/";
	
	public static final String KEYCLOAK_URL 			= KEYCLOAK_SERVER + "auth/";
	public static final String KEYCLOAK_BASE_URL 		= KEYCLOAK_URL + "realms/";
	public static final String KEYCLOAK_CALLBACK_URL 	= "/callback";
	
	public static final String KEYCLOAK_AUTH_URL 		= "/protocol/openid-connect/auth";
	public static final String KEYCLOAK_TOKEN_URL 		= "/protocol/openid-connect/token";
	public static final String KEYCLOAK_SPEC_URL 		= "/protocol/openid-connect/token/introspect";
	
	public static final String KEYCLOAK_TOKEN_TYPE_HINT	= "token_type_hint";
	
	public static final String KEYCLOAK_USER_ID 		= "asan";
	public static final String KEYCLOAK_USER_PW 		= "123";

	public static final String E00000		 		= "00000";
	public static final String E00000_MSG	 		= "성공";
	public static final String E40001		 		= "40001";
	public static final String E40001_MSG	 		= "요청 파라미터 목록이 없습니다.";
	public static final String E40002		 		= "40002";
	public static final String E40002_MSG	 		= "필수 헤더항목이 없습니다.";
	public static final String E40003		 		= "40003";
	public static final String E40003_MSG	 		= "관리자 비밀번호가 일치하지 않습니다.";
	public static final String E50001		 		= "50001";
	public static final String E50001_MSG	 		= "다른 테이블이 참조하는 데이터는 삭제할 수 없습니다.";
	public static final String E50002		 		= "50002";
	public static final String E50002_MSG	 		= "참조 데이터가 없습니다.";
	public static final String E50003		 		= "50003";
	public static final String E50003_MSG	 		= "관리자 비밀번호가 일치하지 않습니다.";
	public static final String E50004		 		= "50004";
	public static final String E50004_MSG	 		= "필요한 데이터가 없습니다.";
	public static final String E50000		 		= "50000";

}
