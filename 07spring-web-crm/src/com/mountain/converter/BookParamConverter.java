package com.mountain.converter;

import com.mountain.model.Book;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.Annotation;

/**
 * 自定义convert生效情景：
 * 1. 对请求的接口包含有需要解析的Book对象
 *  - 若接口中有多个对象则会全部被解析成同一个
 * 2. 传入的请求参数带有对应请求参数名，且能成功被解析
 *  - 若没有，使用@RequestParam指定，则默认的是类名，而不是声明的参数名
 */
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
        return HIGHEST_PRECEDENCE;
    }
}
