package com.mountain.model;

public class Book {

    private String bookName;

    private String author;

    private int age;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    private Content content;

    public Book() {

    }

    public Book(String bookName) {
        System.out.println("== 对象构造器执行");
        this.bookName = bookName;
    }

    public Book(String bookName, String author, int age) {
        this.bookName = bookName;
        this.author = author;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookName() {
        return bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void initMethod(){
        System.out.println("对象初始化方法执行");
    }

    public void destoryMethod(){
        System.out.println("对象销毁方法执行");
    }

    @Override
    public String toString() {
        return "作者：" + getAuthor() + "; 书名：" + getBookName() + "; 年龄:" + getAge();
    }
}
