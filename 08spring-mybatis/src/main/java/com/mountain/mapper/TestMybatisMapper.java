package com.mountain.mapper;

import com.mountain.model.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMybatisMapper {

    String selectStudentNameById(Integer id);

    Student selectStudentById(Integer id);

    Student selectStudentByNameAndAge(@Param("studentName") String studentName,
                                      @Param("age") Integer age);
}
