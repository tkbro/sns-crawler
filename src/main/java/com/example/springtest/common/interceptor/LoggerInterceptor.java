package com.example.springtest.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        logger.info(" Request Address : " + request.getRemoteAddr()
                    + " \tRequest URI : " + request.getRequestURI() + " \tServlet URI : " + request.getServletPath()
                    + " \tRequest URL : " + request.getRequestURL()
                    + " \tResponse Status : " + response.getStatus() + " \tHTTP Method : " + request.getMethod());
    }
}
