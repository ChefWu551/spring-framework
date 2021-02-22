package com.mountain.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(2)
public class AroundAspect {

    @Pointcut("execution(public int com.mountain.*.Calculator.*(int, int)) || execution(public int com.mountain.*.Calculator.sub(int, int))")
    void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object aroundCalculate(ProceedingJoinPoint joinPoint) {

        System.out.println(" around before: 环绕通知开始，方法名称：" + joinPoint.getSignature().getName() + "; 方法参数：" + Arrays.asList(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed(joinPoint.getArgs());
            System.out.println("around after returning, result is +" + result);
            return result;
        } catch (Throwable throwable) {
            System.out.println("around throwing, 默认返回结果0");
            throwable.printStackTrace();
            return 0;
        } finally {
            System.out.println("around after");
        }
    }
}
