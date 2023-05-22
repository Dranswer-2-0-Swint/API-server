package com.t3q.dranswer.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TodoAOP {

    @Pointcut("execution(* com.t3q.dranswer.controller..*.*(..))")
    private void cut(){}

//    @Before("cut()")
//    public void httpgetheader(JoinPoint joinPoint){
//        Gson gson = new Gson();
//        log.info("this is header log");
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                .getRequest();
//
//        Object[] objArr = joinPoint.getArgs();
//        if (objArr != null && objArr.length > 0) {
//
//            for (Object obj : objArr) {
//                if (obj instanceof TodoEntity) {
//                    log.info("[REQUEST] \n [{}][{}][{}][{}] : \n {}", request.getHeader("X-HIT-Log-Key"), request.getProtocol(), request.getMethod(),
//                            request.getServletPath(), gson.toJson((TodoEntity) obj).toString());
//                }
//                else if(obj instanceof TodoEntity) {
//                    log.info("[REQUEST] \n [{}][{}][{}][{}] : \n {}", request.getHeader("X-HIT-Log-Key"), request.getProtocol(), request.getMethod(),
//                            request.getServletPath(), gson.toJson((TodoRequest) obj).toString());
//                }
//            }
//        }
//    }
    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());
        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("=no parameter=");
        for (Object arg : args) {
            log.info("=parameter type = {}=", arg.getClass().getSimpleName());
            log.info("=parameter value = {}=", arg);
        }
    }


    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());

        log.info("=return type = {}=", returnObj.getClass().getSimpleName());
        log.info("=return value = {}=", returnObj);
    }


    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
