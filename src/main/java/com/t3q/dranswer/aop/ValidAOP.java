package com.t3q.dranswer.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.AuthConstants;
import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.dto.keycloak.KeycloakIntroSpectRes;
import com.t3q.dranswer.dto.keycloak.KeycloakTokenRes;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Slf4j
@Aspect
@Component
public class ValidAOP {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public ValidAOP(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Around("@annotation(com.t3q.dranswer.aop.annotation.SwintValid)")
    public Object SwintAuth(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //TODO getHeader 부분 "access_token" 으로 변경할 것
        String token = request.getHeader("access_token");
        String request_id = request.getHeader("request_id");
        //token = token.substring(7);
        boolean isValidToken = validateToken(token,request_id);

        if(!isValidToken){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        return joinPoint.proceed();

    }



    private boolean validateToken(String token, String request_id) throws JSONException, JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("request_id", request_id);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //ClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate resTmpl = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", 		"swint");
        body.add("client_secret", 	"Vvw2Obuuqa4nlAz5cctSBK5kb1jONReP");
        body.add("token_type_hint", "access_token");
        body.add("token", 			token);

        URI uri = UriComponentsBuilder
                .fromUriString(applicationProperties.getAuthUrl() +
                                AuthConstants.KEYCLOAK_BASE_URL +
                                AuthConstants.KEYCLOAK_USER_REALM +
                                AuthConstants.KEYCLOAK_SPEC_URL)
                .build()
                .encode()
                .toUri();

        HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KeycloakIntroSpectRes> entity = resTmpl.postForEntity(   uri,
                                                                                keycloakRequest,
                                                                                KeycloakIntroSpectRes.class);

        //사용자 이름가져오기
        String req_user = entity.getBody().getPreferredUsername();

        //swint token 발급 및 threadlocal에 저장
        String swintToken = getToken(request_id);

        RequestContext.setContextData(request_id,swintToken,req_user);
        if(entity.getBody().isActive()) return true;
        return false;
    }

    private String getToken(String request_id){

        HttpHeaders headers = new HttpHeaders();
        headers.add("request_id", request_id);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RestTemplate resTmpl = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", 		"swint");
        body.add("client_secret", 	"K91G3XhKY3z2qussprBEAC24cksv0qNk");
        body.add("username",        "swint-dev");
        body.add("password",        "1234");
        body.add("grant_type",      "password");

        URI uri = UriComponentsBuilder
                .fromUriString(applicationProperties.getAuthUrl() +
                                AuthConstants.KEYCLOAK_BASE_URL +
                                AuthConstants.KEYCLOAK_SYSTEM_REALM +
                                AuthConstants.KEYCLOAK_TOKEN_URL)
                .build()
                .encode()
                .toUri();

        HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KeycloakTokenRes> entity = resTmpl.postForEntity(uri,
                                                                        keycloakRequest,
                                                                        KeycloakTokenRes.class);
        String swintToken = entity.getBody().getAccessToken();
        return swintToken;
    }
}
