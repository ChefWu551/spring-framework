package com.mountain.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Aspect
//@Component
//@Order(1)
public class MyAspect {

    /**
     * Pointcut：声明切点位置
     * 权限符public可以不写，默认 public
     * 返回类型int可以用*号替代，表示任意类型
     * 包可以用*表示任意一层路径，或..替代，表示对应路径下的所有：com.com.mountain..Calculator.*(int, int)
     * 参数中 * 表示任意一个类型，而..则表示任意数量 com.com.mountain.service.Calculator.*(..)
     *
     * 符号："||"和"|"和"&&"分别表示：或/非/且
     *
     */
    @Pointcut("execution(public int com.mountain.*.Calculator.*(int, int)) || execution(public int com.mountain.*.Calculator.sub(int, int))")
    void pointCut(){

    }

    @Before(value = "pointCut()")
    public void beforeCalculate(JoinPoint joinPoint) {
        System.out.println("myAspect beforeCalculate 入参：" + Arrays.asList(joinPoint.getArgs()) + "; 方法名称：" + joinPoint.getSignature().getName() );
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public Object afterReturningCalculate(JoinPoint joinPoint, int result) {
        System.out.println("myAspect afterReturning: " + result);

        return result;
    }

    @After("execution(public int com.com.mountain.service.Calculator.*(int, int))")
    public void afterCalculate() {
        System.out.println("myAspect after");
    }

    // 只有通过环绕通知才可捕获处理异常
    @AfterThrowing(value = "execution(public int com.com.mountain.service.Calculator.*(int, int))", throwing = "exception")
    public Object afterThrowingCalculate(JoinPoint joinPoint, Exception exception) throws Exception {
        System.out.println("myAspect afterThrowing, 异常信息如下：");
        System.out.println(exception.toString());
        return 0;
    }


}
