package com.mountain.factory;

import com.mountain.model.Book;

public class NewInstanceFactory {

    public Book getInstance(String bookName) {
        return new Book(bookName);
    }
}
