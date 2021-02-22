package com.mountain;

import com.mountain.service.Calculator;
import com.mountain.service.serviceImpl.MyCalculator;
import com.mountain.service.serviceImpl.ServiceTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Service;

public class Main05 {

    /**
     * @Task: AOP 实验
     *
     * 实验一：spring+proxy.newInstance
     * 实验二：spring+cglib增强
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config05.xml");
        /**
         *
         * 实验一：spring+proxy.newInstance
         * 注意:
         *  - 这里spring使用的是jdk的proxy包，所以getBean应该是代理对象（Calculator）而不是实际对象（MyCalculator），否则报错，
         *  - 若有两个类都实现了Calculator，则 NoUniqueBeanDefinitionException: No qualifying bean of type 'com.mountain.service.Calculator' available: expected single matching bean but found 2: myAOtherCalculator,myCalculator
         *  - 若织入了aop的增强，则此时获取的bean必须是代理对象，实际对象是不能通过BeanFactory的getName/getType来获取得到的
         */
/*        Calculator calculator = applicationContext.getBean(Calculator.class);
        // 注意此时对象类型是：对象类是：class com.sun.proxy.$Proxy20[代理对象]
        System.out.println("获取的对象是：" + calculator + "; 对象类是：" + calculator.getClass());
        calculator.add(1, 2);
        // 异常情况
//        calculator.div(1, 0);*/

        /**
         *
         * 实验二：若使用spring+默认的切面代理包会抛出异常，因为代理对象必须是接口，而不能是它本身
         * todo:增强做实际校验
         */
        MyCalculator myCalculator = applicationContext.getBean(MyCalculator.class);
        System.out.println(myCalculator.getClass());
        System.out.println(myCalculator.add(1, 1));

    }
}
