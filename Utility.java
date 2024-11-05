package com.onward.hrservice.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Utility {

	public static final String STATUS = "status";
	public static final String FAILED = "failed";
	public static final String DESCRIPTION = "description";
	public static final String VERSION = "version";
	public static final String SUCCESS_MESSAGE = "success";

	private static final String[] HEADERCELL = { "HRBP", "Employee Code", "Employee Name", "Employee Status", "Grade",
			"Designation", "Category", "RM Code", "Reporting Manager", "Skill Set", "Cost Allocation", "Entity",
			"Business Unit", "Regional Business Unit", "Business Function", "Department ID", "Department Name",
			"Industry Group", "Sub-Industry Vertical", "Employee Base Location", "Engagement Model", "Revenue Category",
			"Line Of Business", "Sub LOB", "Customer Name", "Customer Code", "PD Code", "Customer Country",
			"Customer State", "DOJ", "Initial DOJ", "Gender", "DOB", "Age", "Previous Experience",
			"Relevant Experience", "Onward Experience", "Initial Onward Experience", "Total Experience",
			"Qualification", "Specialization", "Additional Certification", "Confirmation Date", "Confirmation Status",
			"Notice Period", "PAN Number", "Marital Status", "Blood Group", "Personal Email ID",
			"Offical Email Address", "Present Address", "Permanent Address", "Primary Contact Number",
			"Secondary Contact Number", "Father/Husband Name", "Emergency Contact Name",
			"Emergency Contact Relationship", "Emergency Contact Number", "Passport Status", "Passport No",
			"Passport Issue Date", "Passport Expiry Date", "Passport Country", "Visa Type", "Visa Start Date",
			"Visa End Date", "Bond Applicable", "Amount of Bond", "Bond Start Date", "Bond End Date", "PF. NO.",
			"UAN NO.", "ESIC NO.", "Aadhaar Number", "Cost for Budget", "PAN Number(Check)", "UAN Number(Check)",
			"Aadhaar Number(Check)", "Deputation Start Date", "Deputation End Date","Intra Company Transfer", "Exact Work Location", "State",
			"Resignation Date", "Tentative Last Working Day", "Last Working Day", "LWD Month", "Reason",
			"Gratuity Formula", "FnF Status" ,"Mnh ID","Name Asper Bank","Bank Name","Bank Account Number","IFSC Code","Len"};

	public static Map<String, Object> fail(String msg) {
		Map<String, Object> map = new HashMap<>();
		map.put(STATUS, FAILED);
		map.put(DESCRIPTION, msg);
		map.put(VERSION, "1.0");
		return map;
	}

	public static Map<String, Object> success(Object data) {
		Map<String, Object> map = new HashMap<>();
		map.put(STATUS, SUCCESS_MESSAGE);
		map.put("data", data);
		map.put(VERSION, "1.0");
		return map;
	}

	public static LocalDateTime getDateFormat(String date) {
		if (date != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate startDate = LocalDate.parse(date, formatter);
			return startDate.atStartOfDay();
		} else {
			return null;
		}
	}

	public static LocalDate getLocalDateFormat(String date) {
		if (date != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate startDate = LocalDate.parse(date, formatter);
			return startDate;
		} else {
			return null;
		}
	}

	public static String[] getHeadercell() {
		return HEADERCELL;
	}

	public static String checkNullandTrim(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		} else {
			return value.trim();
		}
	}

	public static String combineAddress(String address1, String address2, String address3, String pincode) {
		String combinedAddress = checkNullandTrim(address1) + ", " + checkNullandTrim(address2) + ", "
				+ checkNullandTrim(address3) + ", " + checkNullandTrim(pincode);
		return combinedAddress.contains(", , , ") ? "" : combinedAddress;
	}

	public static String decode(String encoded) {
		String decodedOutput = "";
		try {
			decodedOutput = new String(Base64.getDecoder().decode(encoded));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decodedOutput;
	}

}
