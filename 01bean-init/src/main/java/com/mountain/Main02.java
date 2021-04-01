package com.mountain;

import com.mountain.model.Book;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main02 {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config02.xml");
        // 顺序 执行构造方法-实例化-销毁bean
        // 静态工厂实例化
        Book book01 = (Book) applicationContext.getBean("book01");
        System.out.println(book01);
        System.out.println("== 静态工厂结束");
        applicationContext.close();
        // 实例化工厂
        /*Book book02 = (Book) applicationContext.getBean("book02");
        System.out.println(book02);*/

        // 静态工厂方法生成的实例是否同一个
  /*      Book book04 = (Book) applicationContext.getBean("book01");
        Book book05 = (Book) applicationContext.getBean("book01");
        System.out.println("== 是否同一个实例：");
        System.out.println(book04 == book05);*/

        // xml中${}与#{}的使用
  /*      DruidDataSource dataSource = applicationContext.getBean(DruidDataSource.class);
        System.out.println(dataSource);

        Book book03 = (Book) applicationContext.getBean("book03");
        System.out.println(book03);*/
    }
}
