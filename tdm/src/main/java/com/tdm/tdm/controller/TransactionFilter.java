package com.tdm.tdm.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class TransactionFilter implements Filter {

	
    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        //System.out.println("Starting a transaction for req : {}"+req.getRequestURI());
 
        chain.doFilter(request, response);
        //System.out.println("Starting a transaction for req : {}"+req.getRequestURI());
    }

    // other methods 
}
