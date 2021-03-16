package com.mountain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("pages")
public class HelloController {

    @RequestMapping(value = {"hello", "hello1"}, method = RequestMethod.GET)
    public String success() {
        System.out.println("调用成功");
        return "success";
    }

    @RequestMapping(value = "user/{id}")
    public void getUser(@PathVariable Integer id) {
        System.out.println("用户id: " + id);
    }

    /**
     * params: The parameters of the mapped request, narrowing the primary mapping.
     * 带参,缩小mapping范围,有且只有含有对应的参数才能被找到当前参数
     *
     * headers:同params,对请求头进行限制
     * consumes: 规定http的请求的<b>实体首部</b>内容格式 consumes = {"text/plain", "application/*"}
     * produces: 规定http的响应的<b>实体首部</b>内容格式 consumes = {"text/plain", "application/*"}
     * @param id
     *
     * @Request:
     * url: http://localhost:8080/pages/user/path/123?test=1&test02=1&test03=124&urlParameter=qqq
     * header:
     *  Cookie=>cookieParameter=qwe
     *  User-Agent=>Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36
     *  headerParameter=>ggg
     */
    @RequestMapping(path = "user/path/{id}",
            params = {"test", "!test01", "test02=1", "test03!=123"},
            headers = {"User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36"},
            method = RequestMethod.POST
            )
    public void getUserPath(@PathVariable(required = true) Integer id,
                            @PathVariable(required = false) Integer money,
                            @RequestParam String urlParameter,
                            @RequestHeader String headerParameter,
                            @CookieValue String cookieParameter
//            , @RequestBody Book book
                            ) {
        System.out.println("用户id: " + id);
        System.out.println();

        System.out.println("urlParameter: " + urlParameter);
        System.out.println();

        System.out.println("headerParameter: " + headerParameter);
        System.out.println();

        System.out.println("cookieParameter: " + cookieParameter);
        System.out.println();

//        System.out.println(book.toString());
    }

    /**
     * SpringMVC 可以直接再参数上写原生API
     * HttpServletRequest
     * HttpSession
     * url: http://localhost:8080/pages/origin
     */
    @RequestMapping("origin")
    public String originRequest(HttpServletRequest request, HttpSession session){
        request.setAttribute("requestValue", "ppp");
        return "success";
    }
}
