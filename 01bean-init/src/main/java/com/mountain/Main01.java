package com.mountain;

import com.mountain.model.Book;
import com.mountain.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main01 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        // 获取bean
        System.out.println("== 获取bean == ");
        Book book = (Book) applicationContext.getBean("book");
        System.out.println(book.toString());

        // 是否是单例，默认是单例，当scope="prototype"则是多实例
        Book book1 = (Book) applicationContext.getBean("book");
        System.out.println("== 是否是单例 == ");
        System.out.println(book == book1);

        // 构造方法构建bean对象赋值
        System.out.println("== 构造方法构建bean对象赋值：constructor-arg ==");
        Book book01 = (Book) applicationContext.getBean("book01");
        System.out.println(book01.toString());

        // 嵌套对象list/map/book
        System.out.println("== 嵌套对象list/map/book ==");
        Person person = (Person) applicationContext.getBean("person01");
        System.out.println(person.toString());

        // 配置xml引用对象
//        System.out.println("== 配置xml引用对象 ==");
//        Book bookTemplate = (Book) applicationContext.getBean("bookTemplate");
//        System.out.println(bookTemplate.toString());
    }
}
