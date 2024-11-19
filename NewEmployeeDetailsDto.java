package com.onward.hrservice.dto;

import lombok.Data;

@Data
public class NewEmployeeDetailsDto {
	
	private BasicDetailsDto basicDetails;
	private BusinessDetailsDto businsessDetails;
	private AddressDetailsDto addressDetails;
	private PassportDetailsDto passportDetails;
	private VisaDetailsDto visaDetails;
	private EmergencyContactDetailsDto emergencyContactDetails;
	private EmployeeBankDetailsDto bankDetails;
}
