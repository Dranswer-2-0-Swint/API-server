package com.t3q.dranswer.aop;

import com.t3q.dranswer.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class AuthAOP {
    @Autowired
    private KeycloakService keycloakService;

    //@Pointcut("execution(* com.t3q.dranswer.controller..*.*(..))")

    @Before("@annotation(com.t3q.dranswer.aop.annotation.SwintAuth)")
    public void SwintAuth(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
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
            throw new Exception("토큰이 없습니다");
        }
    }

}
