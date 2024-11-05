package com.onward.hrservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onward.hrservice.dto.EmployeeDetailsDto;
import com.onward.hrservice.service.HRExportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HRExportController {

	public static final String ATTACHEMENT = "attachment; filename=";
	public static final String APPLICATION = "application";
	public static final String MEDIATYPE = "vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Autowired
	HRExportService hrExportService;

	@PostMapping("exportAll")
	public ResponseEntity<byte[]> getDashboardList() {
		log.info("Start exporting All employees");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION, MEDIATYPE));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHEMENT + "exportAll.xlsx");
		byte[] result = null;
		try {
			result = hrExportService.excelData();
		} catch (IOException e) {
			return new ResponseEntity<>(null, headers, HttpStatus.OK);
		}
		return new ResponseEntity<>(result, headers, HttpStatus.OK);
	}

	@PostMapping("exportWithFilters")
	public ResponseEntity<byte[]> getDashboardList(@RequestBody EmployeeDetailsDto data) {
		log.info("Start exporting All employees");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION, MEDIATYPE));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHEMENT + "export.xlsx");
		byte[] result = null;
		try {
			result = hrExportService.excelDataWithFilters(data.getEmployeeCodes());
		} catch (IOException e) {
			return new ResponseEntity<>(null, headers, HttpStatus.OK);
		}
		return new ResponseEntity<>(result, headers, HttpStatus.OK);
	}

}
