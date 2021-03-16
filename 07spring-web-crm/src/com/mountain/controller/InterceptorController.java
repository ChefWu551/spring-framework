package com.mountain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 拦截器使用
 */
@Controller
public class InterceptorController {

    @RequestMapping("interceptor")
    public String interceptor() {
        System.out.println("方法被调用了");
        return "success";
    }
}
