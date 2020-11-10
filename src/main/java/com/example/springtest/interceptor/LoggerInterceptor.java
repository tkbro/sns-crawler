package com.example.springtest.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(" Class : " + (handler.getClass()));
        logger.info(" Request URI : " + (request.getRequestURI()));
        logger.info(" Servlet URI : " + (request.getServletPath()));
        logger.info(" HttpServletMapping : " + (request.getHttpServletMapping()));
        logger.info(" Request URL : " + (request.getRequestURL()));
        logger.info(" response Status : {}", (response.getStatus()));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
