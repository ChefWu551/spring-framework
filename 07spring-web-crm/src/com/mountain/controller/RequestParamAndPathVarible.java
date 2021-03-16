package com.mountain.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

//@Controller
@RestController
public class RequestParamAndPathVarible {

    @RequestMapping("requestParam")
    public String requestParam(@RequestParam(required = false, defaultValue = "nameDefault") String name, @RequestParam Map<String, Integer> character) {
        System.out.println("name: " + name + "; character: " + character);
        return "hahaha";
    }

    @RequestMapping("pathVariable/{name}")
    public JSONObject pathVariable(@PathVariable String name, @PathVariable Map<String, Integer> character) {
        System.out.println("name: " + name + "; character: " + character);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "王刚");
        jsonObject.put("age", 23);
        return jsonObject;
    }

}
