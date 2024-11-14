package com.booking.servicebookingsys.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// This Class allow angular API to access the APIs of our spring boot app.

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)  // 建立Bean執行順序的優先權種
public class SimpleCorsFilter implements Filter {

    private String clientAppUrl = "";

    public SimpleCorsFilter(){

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)  res;
        HttpServletRequest request = (HttpServletRequest) req;
        Map<String, String> map = new HashMap<>();
        String originHeader = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", originHeader);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");  // API
        response.setHeader("Access-Control-Max-age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        // 忽略大小寫比較 request.getMethod() 是否為 "OPTIONS"
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            filterChain.doFilter(req, res); // sent to controller
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
