package com.onward.hrservice.filter;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private int code;
	private String message;

}
