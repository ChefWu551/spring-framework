package com.mountain.service.serviceImpl;

import com.mountain.mapper.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonService<T> {

    @Autowired
    BaseDao<T> baseDao;

    public void save(){
        baseDao.save();
    }
}
