package com.t3q.dranswer.config;

public class AuthConstants {

	// token
	public static final String ACCESS_TOKEN_NAME		= "dr_access_token";
	public static final String REFRESH_TOKEN_NAME		= "dr_refresh_token";
	
	// keycloak
	public static final String KEYCLOAK_USER_REALM 		= "service-user-dev";
	public static final String KEYCLOAK_USER_CLIENT 	= "login";
	public static final String KEYCLOAK_USER_SECRET 	= "JG6eaVSJVzbUp5Sgp7MAyByrraZX7xNC";
	public static final String KEYCLOAK_SYSTEM_REALM 	= "service-system-dev";
	public static final String KEYCLOAK_SYSTEM_CLIENT 	= "swint";
	public static final String KEYCLOAK_SYSTEM_SECRET 	= "K91G3XhKY3z2qussprBEAC24cksv0qNk";
	public static final String KEYCLOAK_SERVER 			= "https://auth.svc.dranswer.co.kr/";
	public static final String KEYCLOAK_URL 			= KEYCLOAK_SERVER + "auth/";
	public static final String KEYCLOAK_BASE_URL 		= KEYCLOAK_URL + "realms/";
	public static final String KEYCLOAK_CALLBACK_URL 	= "/callback";
	public static final String KEYCLOAK_AUTH_URL 		= "/protocol/openid-connect/auth";
	public static final String KEYCLOAK_TOKEN_URL 		= "/protocol/openid-connect/token";
	public static final String KEYCLOAK_SPEC_URL 		= "/protocol/openid-connect/token/introspect";
	public static final String KEYCLOAK_GRANT_TYPE		= "client_credentials";

}
