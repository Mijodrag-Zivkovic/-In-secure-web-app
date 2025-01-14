package com.News.News.security.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Profile("secure")
@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String ipAddress = httpRequest.getRemoteAddr();
            String requestUri = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            LocalDateTime requestTime = LocalDateTime.now();

            logger.info("Incoming Request Details: Time: {}, IP Address: {}, Method: {}, Request URI: {}",
                    requestTime, ipAddress, method, requestUri);
        }

        chain.doFilter(request, response);
    }
}