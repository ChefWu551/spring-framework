package com.mountain.controller;

import com.mountain.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestBodyAndResponse {

    @RequestMapping("requestBody")
    public String requestBody(@RequestBody Book book) {
        System.out.println("requestBody" + book.toString());
        return "success";
    }

    @RequestMapping("responseBody")
    @ResponseBody
    public String responseBody() {
        System.out.println("requestBody");
        return "success";
    }

}
