package com.mountain.service.serviceImpl;

import com.mountain.service.Calculator;
import org.springframework.stereotype.Service;

@Service
public class MyCalculator implements Calculator {
    public int add(int value, int value1) {
        return value + value1;
    }

    public int div(int value, int value1) {
        try {
            return value / value1;
        } catch (Exception e) {
            System.out.println("除法执行官异常，默认返回0");
             return 0;
        }
    }
}
