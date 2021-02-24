package com.mountain;

import com.mountain.service.serviceImpl.ServiceTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config03.xml");
        ServiceTest serviceTest = applicationContext.getBean(ServiceTest.class);
        serviceTest.getMsg();
        System.out.println(serviceTest.value);
    }
}
