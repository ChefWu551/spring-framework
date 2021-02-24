package com.mountain.service.serviceImpl;

import com.mountain.mapper.DaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTest {

    public int value =5;

    @Autowired
    // daoTest这个id对应DaoTest
    // DaoTest daoTest;
    // daoChildTest这个id对应DaoChildTest
    DaoTest daoChildTest;

    public void getMsg() {
        System.out.println("ServiceTest 执行；");

//        daoTest.printMsg();
        daoChildTest.printMsg();
    }

}
