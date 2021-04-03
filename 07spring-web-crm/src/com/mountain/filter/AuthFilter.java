package com.mountain.filter;


import com.mountain.exception.AuthException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.*;
import java.io.IOException;

public class AuthFilter extends Throwable implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        if (userId == null) {
            System.out.println("用户未认证");

            throw new ServletException("必要的userId");
        }
    }

    @Override
    public void destroy() {

    }
}
