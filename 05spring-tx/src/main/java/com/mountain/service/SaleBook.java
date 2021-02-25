package com.mountain.service;

import com.mountain.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleBook {

    @Autowired
    BookMapper bookMapper;

    @Transactional
    public void saleBook() {
        int id = 5;
        bookMapper.updateLeftAmount(id);
        bookMapper.updateLeftNum(id);
        System.out.println("销售成功；");
    }
}
