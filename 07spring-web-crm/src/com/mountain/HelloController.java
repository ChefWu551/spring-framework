package com.mountain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String success() {
        System.out.println("调用成功");
        return "success";
    }
}
