package com.t3q.dranswer.config;

public class ConstantsToValidation {

	// token
	public static final String ACCESS_TOKEN_NAME		= "dr_access_token";
	public static final String REFRESH_TOKEN_NAME		= "dr_refresh_token";
	
	// keycloak
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
	
	// cman url
	public static final String CMAN_PROJECT_CREATE_URL 				= "/api/v1/init/projects";
	public static final String CMAN_IMAGE_CREATE_URL 				= "/api/v1/images";
	public static final String CMAN_IMAGE_DELETE_URL 				= "/api/v1/images/{image_name}";
	public static final String CMAN_CONTAINER_CREATE_URL 			= "/api/v1/containers";
	public static final String CMAN_CONTAINER_UPDATE_URL 			= "/api/v1/containers/{conid}";
	public static final String CMAN_CONTAINER_DELETE_URL 			= "/api/v1/containers/{conid}";
	public static final String CMAN_CONTAINER_DEPLOY_URL 			= "/api/v1/containers/{conid}/deploy";
	public static final String CMAN_CONTAINER_RECYCLE_URL 			= "/api/v1/containers/{conid}/recycle";
	public static final String CMAN_CONTAINER_DOMAIN_CREATE_URL 	= "/api/v1/containers/{conid}/domain";
	public static final String CMAN_CONTAINER_DOMAIN_DELETE_URL 	= "/api/v1/containers/{conid}/domain";
	public static final String CMAN_CONTAINER_STATUS_READ_URL 		= "/api/v1/containers/status/list";

	// error code
	public static final String E00000		 		= "00000";
	public static final String E00000_MSG	 		= "성공";
	public static final String E40001		 		= "40001";
	public static final String E40001_MSG	 		= "필수 항목이 없습니다.";
	public static final String E40002		 		= "40002";
	public static final String E40002_MSG	 		= "필수 헤더항목이 없습니다.";
	public static final String E40003		 		= "40003";
	public static final String E40003_MSG	 		= "토큰이 유효하지 않습니다.";
	public static final String E50001		 		= "50001";
	public static final String E50001_MSG	 		= "시스템에 접속할 수 없습니다.";
	public static final String E50002		 		= "50002";
	public static final String E50002_MSG	 		= "필요한 데이터가 없습니다.";
	public static final String E50000		 		= "50000";

}
