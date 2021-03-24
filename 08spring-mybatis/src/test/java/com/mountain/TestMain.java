package com.mountain;

import com.mountain.mapper.TestMybatisMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class TestMain {

    private SqlSessionFactory sqlSessionFactory;

    @SneakyThrows
    @Before
    public void initSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test() {
        SqlSession session = sqlSessionFactory.openSession();
        TestMybatisMapper mybatisMapper = session.getMapper(TestMybatisMapper.class);
        System.out.println("学生姓名是： " + mybatisMapper.selectStudentNameById(5));
    }

    @Test
    public void printSelectStudentById() {
        SqlSession session = sqlSessionFactory.openSession();
        TestMybatisMapper mybatisMapper = session.getMapper(TestMybatisMapper.class);
        System.out.println("学生信息是： " + mybatisMapper.selectStudentById(5));
        session.close();
    }

    @Test
    public void printStudentByNameAndAge() {
        SqlSession session = sqlSessionFactory.openSession();
        TestMybatisMapper mybatisMapper = session.getMapper(TestMybatisMapper.class);
        System.out.println("学生信息是： " + mybatisMapper.selectStudentByNameAndAge("xiaoming", 12));
    }
}
