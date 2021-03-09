package com.mountain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FilterInterceptorController {

    @RequestMapping("interceptor")
    public String interceptor() {
        System.out.println("方法被调用了");
        return "success";
    }
}
