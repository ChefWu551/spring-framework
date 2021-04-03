package com.mountain.controller;

import com.mountain.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ioc converter使用
 */
@Controller
public class ConverterBookController {

    @ResponseBody
    @RequestMapping("printBook")
    public String getBook(@RequestParam("bookObject") Book book, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(book.toString());
        return "book" ;
    }

    @RequestMapping("printString")
    public void getBookString(Book book1
            , Book book2
    ) {
        System.out.println("book1: " + book1.toString());
        System.out.println("book2: " + book2.toString());
    }
}
