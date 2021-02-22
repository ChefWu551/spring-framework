package com.mountain;

import com.mountain.service.Calculator;
import com.mountain.service.serviceImpl.MyCalculator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main05 {

    /**
     * @Task: AOP 实验
     *
     * 实验一：spring+proxy.newInstance
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config05.xml");
        // 实验一：spring+proxy.newInstance
        // 注意，这里spring使用的是jdk 的proxy包，所以getBean应该是代理对象（Calculator）而不是实际对象（MyCalculator）
        Calculator calculator = applicationContext.getBean(Calculator.class);
        // 注意此时对象类型是：对象类是：class com.sun.proxy.$Proxy20[代理对象]
        System.out.println("获取的对象是：" + calculator + "; 对象类是：" + calculator.getClass());
//        calculator.add(1, 2);
        // 异常情况
        calculator.div(1, 0);
    }
}
