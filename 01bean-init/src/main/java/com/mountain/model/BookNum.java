package com.mountain.model;


public class BookNum {
    private String bookName;
    private String author;
    private String age;

    public BookNum(String bookName, String author, String age) {
    }

    public BookNum(String bookName, String author) {
    }

    public BookNum() {
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }
}
