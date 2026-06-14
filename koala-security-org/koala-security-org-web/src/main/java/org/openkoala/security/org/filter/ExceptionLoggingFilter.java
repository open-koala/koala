package org.openkoala.security.org.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionLoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (IOException e) {
            logException(request, e);
            throw e;
        } catch (ServletException e) {
            logException(request, e);
            throw e;
        } catch (RuntimeException e) {
            logException(request, e);
            throw e;
        }
    }

    private void logException(ServletRequest request, Exception exception) {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            LOGGER.error("Unhandled request exception: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI(), exception);
            return;
        }
        LOGGER.error("Unhandled request exception", exception);
    }

    @Override
    public void destroy() {
    }
}
