package com.mountain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping("redirectPage")
    public String redirectPage() {
        System.out.println("重定向方法被调用了");
        return "redirect";
    }

    @RequestMapping("redirect")
    public String redirect() {
        return "redirect:redirectPage";
    }

    @RequestMapping("forward")
    public String forward() {
        return "forward:redirectPage";
    }


}
