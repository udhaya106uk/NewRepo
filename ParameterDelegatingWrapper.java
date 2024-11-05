package com.onward.hrservice.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterDelegatingWrapper extends HttpServletRequestWrapper {
	
	private Map<String, String> extraParameters;

	  public ParameterDelegatingWrapper(HttpServletRequest request) {
	    super(request);
	    extraParameters = new HashMap<>();
	  }

	  public ParameterDelegatingWrapper(ServletRequest request) {
	    super((HttpServletRequest) request);
	    extraParameters = new HashMap<>();
	  }

	  @Override
	  public String getParameter(String name) {
	    if (extraParameters.get(name) != null) {
	      log.debug("{} : {}", name , extraParameters.get(name));
	      return extraParameters.get(name);
	    } else {
	      log.debug("{} : {}", name , extraParameters.get(name));
	      return super.getParameter(name);
	    }
	  }

	  public void setParameter(String name, String value) {
	    extraParameters.put(name, value);
	  }
}
