package com.mountain.mapper;

import com.mountain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User> {
    @Override
    public void save() {
        System.out.println("UserDao print");
    }
}
