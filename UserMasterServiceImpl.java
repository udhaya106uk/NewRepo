package com.onward.hrservice.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onward.hrservice.common.Utility;
import com.onward.hrservice.dto.UserMasterDto;
import com.onward.hrservice.entity.UserMaster;
import com.onward.hrservice.repository.UserMasterRepository;
import com.onward.hrservice.service.UserMasterService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	UserMasterRepository userMasterRepo;

	@Override
	public Map<String, Object> saveRegistration(UserMasterDto data) {
		log.info("Start Saving User Registation");
		UserMaster users = userMasterRepo.findByEmpCode(data.getEmpCode());
		if(users != null) {
			return Utility.fail("User Registration already present for this Employee Code");
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserMaster userMaster = modelMapper.map(data, UserMaster.class);
		UserMaster user = userMasterRepo.findTopByOrderByUserCodeDesc();
		userMaster.setUserName(data.getFirstName() + " " + data.getLastName());
		userMaster.setUserPwExpiryPeriod(30);
		userMaster.setUserCreatedDate(LocalDateTime.now());
		userMaster.setCreatedOn(LocalDateTime.now());
		userMaster.setOrgCode(1);
		userMaster.setStatus("A");
		userMaster.setCreatedBy(user.getUserCode()+1);
		if (data.getUserPassword() != null) {
			passwordEncryption(data, userMaster);
		}
		userMasterRepo.save(userMaster);
		log.info("End User Registration Saved");
		return Utility.success("User Registration Created Successfully");
	}

	private void passwordEncryption(UserMasterDto data, UserMaster userMaster) {
		/* Plain-text password initialization. */
		String encryptedpassword = null;
		try {
			/* MessageDigest instance for MD5. */
			MessageDigest m = MessageDigest.getInstance("MD5");
			/* Add plain-text password bytes to digest using MD5 update() method. */
			m.update(data.getUserPassword().getBytes());
			/* Convert the hash value into bytes */
			byte[] bytes = m.digest();
			/*
			 * The bytes array has bytes in decimal form. Converting it into hexadecimal
			 * format.
			 */
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			/* Complete hashed password in hexadecimal format */
			encryptedpassword = s.toString();
			userMaster.setUserPassword(encryptedpassword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Object> userLogin(UserMasterDto data) {
		log.info("Login started");
		UserMaster users = userMasterRepo.findByEmpCode(data.getEmpCode());
		log.info("User Info from DB ::{}", users);
		if (users == null) {
			return Utility.fail("User ID does not Exists");
		}
		Map<String, Object> map = new HashMap<>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserMaster userMaster = modelMapper.map(data, UserMaster.class);
		if (data.getUserPassword() != null) {
			passwordEncryption(data, userMaster);
		}
		if (userMaster.getEmpCode().equals(users.getEmpCode())
				&& userMaster.getUserPassword().equals(users.getUserPassword())) {
			map.put("userID", users.getUserCode().toString());
			log.info("Login sucess");
			return Utility.success(map);
		}
		log.info("Login Failed");
		return Utility.fail("User ID or Password is Incorrect");

	}

	@Override
	public Map<String, Object> updatePassword(UserMasterDto data) {
		UserMaster users = userMasterRepo.findByEmpCode(data.getEmpCode());
		log.info("User Details");
		if(users == null) {
			return Utility.fail("User ID does not Exists");
		}
		if(!data.getValidationCode().equalsIgnoreCase(users.getUserPwSecretKey())) {
			return Utility.fail("Verification code does not Match");
		}
		log.info("Password Encryption");
		if (data.getUserPassword() != null) {
			passwordEncryption(data, users);
		}
		users.setModifiedBy(users.getUserCode());
		users.setModifiedOn(LocalDateTime.now());
		users.setUserPwChangedDate(LocalDateTime.now());
		userMasterRepo.save(users);
		log.info("Password Updated");
		return Utility.success("User Password Changed Successfully");
	}
}
