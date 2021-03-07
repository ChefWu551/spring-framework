package com.mountain;

import com.mountain.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

    @RequestMapping("printBook")
    public void getBook(@RequestParam("bookObject") Book book) {
        System.out.println(book.toString());
    }

    @RequestMapping("printString")
    public void getBookString(Book book1) {
        System.out.println(book1.toString());
    }
}
