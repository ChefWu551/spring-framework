package com.mountain.component;

import com.mountain.viewResolver.MyViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MyViewResolver myViewResolver = new MyViewResolver();
        registry.viewResolver(myViewResolver);
    }
}
