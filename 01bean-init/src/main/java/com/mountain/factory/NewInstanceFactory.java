package com.mountain.factory;

import com.mountain.model.Book;

public class NewInstanceFactory {

    public Book getInstance(String bookName, String author, int age) {
        return new Book(bookName, author, age);
    }
}
