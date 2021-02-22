package com.mountain.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DaoChildTest extends DaoTest {

    @Override
    public void printMsg() {
        System.out.println("DaoChildTest: 打印信息。。");
    }
}
