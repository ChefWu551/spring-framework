package com.mountain;

import com.mountain.service.serviceImpl.BookService;
import com.mountain.service.serviceImpl.UserService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main04 {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config04.xml");
        BookService bookService = (BookService) applicationContext.getBean("bookService");
        bookService.save();

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.save();
    }
}
