package com.mountain.controller;

import com.mountain.exception.ArithmaticException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义异常
 */
@Controller
public class ExceptionController {

    @RequestMapping("arithmatic")
//    @ExceptionHandler(ArithmaticException.class)
    public void mathException(@RequestParam int value1, @RequestParam int value2){
        int i = value1 / value2;
        System.out.println(value1 / value2);
    }
}
