package com.mountain.model;

import lombok.Data;

import java.util.List;

@Data
public class Teacher {

    private String name;

    private Integer age;

    private String course;

    private List<Student> students;

}
