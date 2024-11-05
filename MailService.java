package com.onward.hrservice.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

public interface MailService {
	
	public Map<String, Object> sendForgotPassword(String toRecipient, Integer empNumber) throws MessagingException, UnsupportedEncodingException ;

}
