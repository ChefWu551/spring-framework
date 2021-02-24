package com.mountain.factory;

import com.mountain.model.Book;

public class StaticInstanceFactory {

    public static Book getBookInstance(String bookName) {
        System.out.println("静态工厂方法执行");
        return new Book(bookName);
    }
}
