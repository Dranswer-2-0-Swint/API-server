package com.t3q.dranswer.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.t3q.dranswer.service.KeycloakService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ValidAOP {
    @Autowired
    private KeycloakService keycloakService;


    //@Pointcut("execution(* com.t3q.dranswer.controller..*.*(..))")

    @Before("@annotation(com.t3q.dranswer.aop.annotation.SwintValid)")
    public void SwintAuth(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            boolean result = false;
            try {
                result = keycloakService.getAuthorizationByHeader(request);
                if (result == false) {
                    log.error("getKeycloakTokenByAuthorizationCode()");
                    throw new Exception("토큰이 유효하지 않습니다");

                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new Exception("토큰이 유효하지 않습니다");
            }
        } else {
            throw new Exception("토큰이 헤더에 존재하지 않습니다.");
        }
    }
}
