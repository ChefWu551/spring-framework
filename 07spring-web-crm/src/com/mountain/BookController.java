package com.mountain;

import com.mountain.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BookController {

    @RequestMapping("printBook")
    public String getBook(@RequestParam("bookObject") Book book, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(book.toString());
        return "book" ;
    }

    @RequestMapping("printString")
    public void getBookString(Book book1) {
        System.out.println(book1.toString());
    }
}
