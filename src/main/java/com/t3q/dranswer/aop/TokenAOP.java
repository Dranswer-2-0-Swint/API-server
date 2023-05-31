package com.t3q.dranswer.aop;

import com.t3q.dranswer.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
public class TokenAOP {

    @Autowired
    private KeycloakService keycloakService;
    @Before("@annotation(com.t3q.dranswer.aop.annotation.GetSwintToken)")
    public void getSwintToken(JoinPoint joinPoint) throws Exception {


    }

}
