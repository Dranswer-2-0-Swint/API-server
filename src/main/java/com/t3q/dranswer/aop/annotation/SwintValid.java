package com.t3q.dranswer.aop.annotation;




import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SwintAuth {
}