package com.t3q.dranswer.aop;

import com.t3q.dranswer.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Aspect
@Component
public class TokenAOP {

    @Autowired
    private KeycloakService keycloakService;
    @Before("@annotation")
    public void getSwintToken(JoinPoint joinPoint) throws Exception {


    }

}
