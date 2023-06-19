package com.t3q.dranswer.service;

import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.AuthConstants;
import com.t3q.dranswer.dto.keycloak.KeycloakTokenRes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Service
public class KeycloakService {

	private final ApplicationProperties applicationProperties;

	@Autowired
	public KeycloakService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	// keycloak login page
	public String getRedirectUrl(HttpServletRequest request) {
		String authUrl = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_AUTH_URL;
		String clientId = AuthConstants.KEYCLOAK_USER_CLIENT;
		String redirectUri = applicationProperties.getCallbackUrl() + AuthConstants.KEYCLOAK_CALLBACK_URL;
		String responseType = "code";
		String url = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=openid",
				authUrl, clientId, redirectUri, responseType);
		return url;
	}

	// 인증코드로 토큰발급
	public boolean getTokenByAuthorizationCode(HttpServletRequest request, String code) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", AuthConstants.KEYCLOAK_USER_CLIENT);
		map.add("client_secret", AuthConstants.KEYCLOAK_USER_SECRET);
		map.add("grant_type", "authorization_code");
		map.add("redirect_uri", applicationProperties.getCallbackUrl() + AuthConstants.KEYCLOAK_CALLBACK_URL);
		map.add("code", code);

		HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(map, headers);
		String url = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_TOKEN_URL;
		try {
			ResponseEntity<KeycloakTokenRes> response = restTemplate.postForEntity(url, keycloakRequest, KeycloakTokenRes.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				request.getSession().setAttribute(AuthConstants.ACCESS_TOKEN_NAME, response.getBody().getAccessToken());
				request.getSession().setAttribute(AuthConstants.REFRESH_TOKEN_NAME, response.getBody().getRefreshToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	// 토큰검증
//	public boolean authorization(HttpServletRequest request) {
//
//		boolean active = true;
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		Object accessToken = request.getSession().getAttribute(AuthConstants.ACCESS_TOKEN_NAME);
//		Object refreshToken = request.getSession().getAttribute(AuthConstants.REFRESH_TOKEN_NAME);
//
//		if (accessToken == null || refreshToken == null) {
//			log.error("no token");
//			return false;
//		}
//
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//		map.add("client_id", 		AuthConstants.KEYCLOAK_USER_CLIENT);
//		map.add("client_secret", 	AuthConstants.KEYCLOAK_USER_SECRET);
//		map.add("token_type_hint",	"access_token");
//		map.add("token",			accessToken.toString());
//
//		HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(map, headers);
//		String url = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_SPEC_URL;
//		try {
//			ResponseEntity<KeycloakIntroSpectRes> response = restTemplate.postForEntity(url, keycloakRequest, KeycloakIntroSpectRes.class);
//
//			if (response.getStatusCode() == HttpStatus.OK) {
//				log.info("active : " + response.getBody().isActive());
//				active = response.getBody().isActive();
//
//				if (active == true) {
//					Map<String, KeycloakIntroSpectRoleRes> jsonMap = response.getBody().getResourceAccess();
//					for (Map.Entry<String, KeycloakIntroSpectRoleRes> entry : jsonMap.entrySet()) {
//						String clientId = entry.getKey();
//						List<String> roles = entry.getValue().getRoles();
//						log.info("clientId : " + clientId);
//						log.info("roles : " + roles.toString());
//					}
//				} else {
//					// clientToken 발급
//					String clientToken = null;
//					MultiValueMap<String, String> getTokenMap = new LinkedMultiValueMap<>();
//					getTokenMap.add("client_id", 		AuthConstants.KEYCLOAK_USER_CLIENT);
//					getTokenMap.add("client_secret", 	AuthConstants.KEYCLOAK_USER_SECRET);
//					getTokenMap.add("grant_type",		"client_credentials");
//
//					HttpEntity<MultiValueMap<String, String>> getTokenRequest = new HttpEntity<>(getTokenMap, headers);
//					url = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_TOKEN_URL;
//					try {
//						ResponseEntity<KeycloakTokenRes> getTokenResponse = restTemplate.postForEntity(url, getTokenRequest, KeycloakTokenRes.class);
//						if (response.getStatusCode() == HttpStatus.OK) {
//							clientToken = getTokenResponse.getBody().getAccessToken();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						log.error(e.getMessage());
//						return false;
//					}
//
//					// refreshToken 발급
//					MultiValueMap<String, String> getNewTokenMap = new LinkedMultiValueMap<>();
//					getNewTokenMap.add("client_id", 		AuthConstants.KEYCLOAK_USER_CLIENT);
//					getNewTokenMap.add("client_secret", 	AuthConstants.KEYCLOAK_USER_SECRET);
//					getNewTokenMap.add("refresh_token", 	refreshToken.toString());
//					getNewTokenMap.add("grant_type",		"refresh_token");
//
//					headers.setBearerAuth(clientToken);
//					HttpEntity<MultiValueMap<String, String>> getNewTokenRequest = new HttpEntity<>(getNewTokenMap, headers);
//					url = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_TOKEN_URL;
//					try {
//						ResponseEntity<KeycloakTokenRes> getNewTokenResponse = restTemplate.postForEntity(url, getNewTokenRequest, KeycloakTokenRes.class);
//						if (getNewTokenResponse.getStatusCode() == HttpStatus.OK) {
//							log.info("new_token_success!");
//							request.getSession().setAttribute(AuthConstants.ACCESS_TOKEN_NAME, getNewTokenResponse.getBody().getAccessToken());
//							request.getSession().setAttribute(AuthConstants.REFRESH_TOKEN_NAME, getNewTokenResponse.getBody().getRefreshToken());
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						log.error(e.getMessage());
//						return false;
//					}
//				}
//			}
//		} catch (HttpClientErrorException e) {
//			e.printStackTrace();
//			log.error(e.getMessage());
//			return false;
//		}
//		return active;
//	}
//
//	//요청 헤더에 있는 토큰검증
//	public boolean getAuthorizationByHeader(String token) {
//
//		//String refreshToken = "getAuthorizationByHeader is not using request token";
//		//if(token == null){
//
//		boolean active = true;
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//		map.add("client_id", 		AuthConstants.KEYCLOAK_USER_CLIENT);
//		map.add("client_secret", 	AuthConstants.KEYCLOAK_USER_SECRET);
//		map.add("token_type_hint", "access_token");
//		map.add("token", 			token);
//
//		HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(map, headers);
//		String url = applicationProperties.getAuthUrl() + AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_SPEC_URL;
//		ResponseEntity<KeycloakIntroSpectRes> response = null;
//		try {
//			response = restTemplate.postForEntity(url, keycloakRequest, KeycloakIntroSpectRes.class);
//		} catch (HttpClientErrorException e) {
//			e.printStackTrace();
//			e.getMessage();
//			//예외처리 400 500 등등
//		}
//		if (response.getStatusCode() == HttpStatus.OK) {
//			log.info("active : " + response.getBody().isActive());
//			active = response.getBody().isActive();
//
//			if (active == true) {
//				Map<String, KeycloakIntroSpectRoleRes> jsonMap = response.getBody().getResourceAccess();
//				for (Map.Entry<String, KeycloakIntroSpectRoleRes> entry : jsonMap.entrySet()) {
//					String clientId = entry.getKey();
//					List<String> roles = entry.getValue().getRoles();
//					log.info("clientId : " + clientId);
//					log.info("roles : " + roles.toString());
//				}
//			}
//			else {
//				return false;
//			}
//		}
//		return active;
//	}
//}
}