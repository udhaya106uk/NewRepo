package com.onward.hrservice.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onward.hrservice.common.Utility;
import com.onward.hrservice.dto.UserMasterDto;
import com.onward.hrservice.service.MailService;
import com.onward.hrservice.service.UserMasterService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("users")
@Slf4j
public class UserMasterController {
	
	@Autowired
	UserMasterService userMasterService;
	
	@Autowired
	MailService mailService;
	
	@PostMapping("registration")
	public Map<String, Object> saveRegistration(@RequestBody UserMasterDto data){
		log.info("User Registration");
		return userMasterService.saveRegistration(data);
	}
	
	@PostMapping("login")
	public Map<String, Object> userLogin(@RequestBody UserMasterDto data) {
		log.info("User Login");
		return userMasterService.userLogin(data);
	}
	
	@PostMapping("forgotpassword")
	public Map<String, Object> forgotPassword(@RequestBody UserMasterDto data){
		log.info("Start Forgot Password API");
		try {
			return mailService.sendForgotPassword(data.getUserEmail(),data.getEmpCode());
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		return Utility.fail("Mail Service Failed");		
	}
	
	@PostMapping("updatePassword")
	public Map<String, Object> updatePassword(@RequestBody UserMasterDto data){
		log.info("Start updating Password");
		return userMasterService.updatePassword(data);
	}

}
