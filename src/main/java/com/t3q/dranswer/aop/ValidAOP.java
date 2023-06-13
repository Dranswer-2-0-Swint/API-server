package com.t3q.dranswer.aop;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3q.dranswer.config.AuthConstants;
import com.t3q.dranswer.dto.keycloak.KeycloakIntroSpectRes;
import com.t3q.dranswer.dto.servpot.ApiResponse;
import com.t3q.dranswer.service.KeycloakService;
import org.apache.tomcat.util.json.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
>>>>>>> main

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Aspect
@Component
public class ValidAOP {



    @Around("@annotation(com.t3q.dranswer.aop.annotation.SwintValid)")
    public Object SwintAuth(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String request_id = request.getHeader("request_id");
        token = token.substring(7);

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

        HttpEntity<MultiValueMap<String, String>> keycloakRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KeycloakIntroSpectRes> entity = resTmpl.postForEntity(URI.create(AuthConstants.KEYCLOAK_BASE_URL + AuthConstants.KEYCLOAK_USER_REALM + AuthConstants.KEYCLOAK_SPEC_URL)
                , keycloakRequest
                , KeycloakIntroSpectRes.class);

        log.info(String.valueOf(entity.getBody()));

        if(entity.getBody().isActive()) return true;
        return false;
    }
}
