package com.mountain.converter;

import com.mountain.model.Book;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.Annotation;

public class BookParamConverter implements Converter<String, Book>, Ordered {

    @Override
    public Book convert(String source) {
        Book book = new Book();
        String[] results = source.split("-");
        book.setBookName(results[0]);
        book.setAuthor(results[1]);
        book.setAge(Integer.valueOf(results[2]));
        return book;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
