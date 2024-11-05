package com.onward.hrservice.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.onward.hrservice.dto.EmployeeDetailsDto;
import com.onward.hrservice.dto.NewEmployeeDetailsDto;
import com.onward.hrservice.exception.HrServiceException;

public interface HrConnectService {
	
	public Map<String, Object> getDashboardList();
	
	public Map<String, Object> getMasterData();
	
	public Map<String, Object> saveEmployee(NewEmployeeDetailsDto data, String userId) throws HrServiceException;
	
	public Map<String, Object> updateEmployee(NewEmployeeDetailsDto data, String userId) throws HrServiceException;
	
	public Map<String, Object> getEmployeeData(String data);
	
	public Map<String, Object> getReportingManager(String data);
	
	public Map<String, Object> getDropDownMapping(String data);
	
	public Map<String, Object> saveAttachment(MultipartFile data, String type, String empNumber);
	
	public Map<String, Object> deleteAttachment(String type, String empNumber);
	
	public byte[] exportAttachment(String type, String empNumber);
	
	public Map<String, Object> getCustomerDetails(String customerName);

	public Map<String, Object> getEmployeeDetailsWithFilterDoj(EmployeeDetailsDto data);

	public Map<String, Object> getEmployeeDetailsWithFilterEmployeeName(EmployeeDetailsDto data);

}
