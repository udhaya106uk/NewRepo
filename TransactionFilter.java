package com.onward.hrservice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransactionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
		res.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

		if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
			// Authorize (allow) all domains to consume the content
			res.setStatus(HttpServletResponse.SC_OK);
			return;
		}
	    String requestURL=req.getRequestURI();
	    log.info("Starting a transaction for req : {}" , requestURL);
	    log.info("Authorization {}",req.getHeader("Authorization"));
	    chain.doFilter(request, response);
	    log.info("Committing a transaction for req : {}" , req.getRequestURI());
	}

}
