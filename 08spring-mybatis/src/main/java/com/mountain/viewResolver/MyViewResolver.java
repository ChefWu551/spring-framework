package com.mountain.viewResolver;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.lang.annotation.Annotation;
import java.util.Locale;

@Component
public class MyViewResolver implements ViewResolver, Order {
    public View resolveViewName(String s, Locale locale) throws Exception {

        if ("myView".equals(s)) {
            System.out.println("欢迎来到自定义视图");
            System.out.println("当前的locale对象：" + locale.toString());

            return new MyView();
        }

        return null;
    }

    public int value() {
        return 1;
    }

    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
