package com.mountain.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookMapper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void updateLeftAmount(int id){
        jdbcTemplate.update("update bookAmount set amount = amount-5 where id=?", id);
    }

    public void updateLeftNum(int id) {
        jdbcTemplate.update("update bookNum set num=num-1 where id=?", id);
    }
}
