package com.onward.hrservice.service;

import java.util.Map;

import com.onward.hrservice.dto.UserMasterDto;

public interface UserMasterService {
	
	public Map<String, Object> saveRegistration(UserMasterDto data);
	
	public Map<String, Object> userLogin(UserMasterDto data);
	
	public Map<String, Object> updatePassword(UserMasterDto data);

}
