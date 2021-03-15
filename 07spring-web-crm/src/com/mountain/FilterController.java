package com.mountain;

import com.mountain.exception.AuthException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * java自带Filter作用
 */
@Controller
public class FilterController {

    @RequestMapping("auth")
    public void print(@RequestParam("userId") String userId) {
        System.out.println("用户id：" + userId);
    }

}
