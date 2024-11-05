package com.onward.hrservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onward.hrservice.dto.EmployeeDetailsDto;
import com.onward.hrservice.dto.NewEmployeeDetailsDto;
import com.onward.hrservice.exception.HrServiceException;
import com.onward.hrservice.service.HrConnectService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HrConnectController {

	@Autowired
	HrConnectService hrConnectService;

	@PostMapping("getDashboard")
	public Map<String, Object> getDashboardList() {
		return hrConnectService.getDashboardList();
	}

	@PostMapping("saveNewEmployee")
	public Map<String, Object> saveEmployee(@RequestBody NewEmployeeDetailsDto data, @RequestParam String userId)
			throws HrServiceException {
		log.info("Save Employee Data ::{}", data);
		return hrConnectService.saveEmployee(data, userId);
	}

	@PostMapping("updateEmployee")
	public Map<String, Object> updateEmployee(@RequestBody NewEmployeeDetailsDto data, @RequestParam String userId)
			throws HrServiceException {
		log.info("Update Employee Data ::{}", data);
		return hrConnectService.updateEmployee(data, userId);
	}

	@GetMapping("getMasterData")
	public Map<String, Object> getMasterData() {
		return hrConnectService.getMasterData();
	}

	@PostMapping("getEmployeeData")
	public Map<String, Object> getEmployeeData(@RequestBody EmployeeDetailsDto data) throws Exception {
		log.info("Get Employee Data ::{}", data);
		return hrConnectService.getEmployeeData(data.getEmployeeId());
	}

	@PostMapping("getReportingManager")
	public Map<String, Object> getReportingManager(@RequestBody EmployeeDetailsDto data) throws HrServiceException {
		log.info("Reporting Manager Name ::{}", data.getEmpName());
		return hrConnectService.getReportingManager(data.getEmpName());
	}

	@PostMapping("getMappingDropDown")
	public Map<String, Object> getSubLob(@RequestBody EmployeeDetailsDto data) {
		return hrConnectService.getDropDownMapping(data.getDropDownCode());
	}

	@PostMapping(value = "fileAttachement/{type}/{empNumber}")
	public Map<String, Object> saveAttachment(@PathVariable("type") String type,@PathVariable("empNumber") String empNumber, @RequestParam("files") MultipartFile multiPartFile) {
		log.info("Attachement Data for Upload Type::{}", type);
		return hrConnectService.saveAttachment(multiPartFile, type, empNumber);
	}

	@PostMapping(value = "deleteAttachement/{type}/{empNumber}")
	public Map<String, Object> deleteAttachment(@PathVariable("type") String type,
			@PathVariable("empNumber") String empNumber) {
		log.info("Attachement Data for Delete Type::{}", type);
		return hrConnectService.deleteAttachment(type, empNumber);
	}

	@GetMapping(value = "exportAttachement/{type}/{empNumber}/{fileName}")
	public ResponseEntity<Object> exportAttachment(@PathVariable("type") String type,
			@PathVariable("empNumber") String empNumber, @PathVariable("fileName") String fileName) {
		log.info("Attachement Data for Export Type::{}", type);
		byte[] result = null;
		try {
			result = hrConnectService.exportAttachment(type, empNumber);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
			headers.add("Content-Disposition", "attachment;filename=" + fileName);
			log.info("result:" + result);
			return new ResponseEntity<>(result, headers, HttpStatus.OK);
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("getCustomerDeatils")
	public Map<String, Object> getCustomerDetails(@RequestBody EmployeeDetailsDto data) {
		log.info("Get Customer Details");
		return hrConnectService.getCustomerDetails(data.getCustomerName());
	}

	@PostMapping("getEmployeeDetailsFilter")
	public Map<String, Object> getEmployeeDetailsWithFilterDoj(@RequestBody EmployeeDetailsDto data) {
		log.info("Get Employee Details Based On Date of Joining");
		return hrConnectService.getEmployeeDetailsWithFilterDoj(data);
	}

	@PostMapping("getEmployeeBySearch")
	public Map<String, Object> getEmployeeDetailsWithFilterEmployeeName(@RequestBody EmployeeDetailsDto data) {
		log.info("Get Employee Details Based On Employee Name OR Employee Number");
		return hrConnectService.getEmployeeDetailsWithFilterEmployeeName(data);
	}

}