package com.zhaoqi.psp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by qi on 17-11-20.
 */
@Aspect
public class DAOInterceptor {

    @Around("execution(* com.zhaoqi.*.dao.*(..))")
    public Object runOnAround(ProceedingJoinPoint point) throws Throwable {
        System.out.println("Start ====" + point.getTarget() + point.getArgs());
        Object object = point.proceed();
        System.out.println("End ====" + point.getSignature());
        return object;
    }
}
