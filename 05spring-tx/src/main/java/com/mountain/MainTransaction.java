package com.mountain;

import com.mountain.service.SaleBook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.view.InternalResourceView;


public class MainTransaction {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config.xml");
        SaleBook saleBook = (SaleBook) applicationContext.getBean("saleBook");
        saleBook.saleBook();
    }
}
