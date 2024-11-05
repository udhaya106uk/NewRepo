package com.onward.hrservice.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.onward.hrservice.common.Utility;
import com.onward.hrservice.constant.DropDownMaster;
import com.onward.hrservice.dto.BasicDetailsDto;
import com.onward.hrservice.dto.BusinessDetailsDto;
import com.onward.hrservice.dto.EmergencyContactDetailsDto;
import com.onward.hrservice.dto.EmployeeBankDetailsDto;
import com.onward.hrservice.dto.EmployeeDashboardDto;
import com.onward.hrservice.dto.EmployeeDetailsDto;
import com.onward.hrservice.dto.MasterDataDto;
import com.onward.hrservice.dto.NewEmployeeDetailsDto;
import com.onward.hrservice.dto.PassportDetailsDto;
import com.onward.hrservice.dto.VisaDetailsDto;
import com.onward.hrservice.entity.DocumentMaster;
import com.onward.hrservice.entity.EmployeeAddressDetails;
import com.onward.hrservice.entity.EmployeeBankDetails;
import com.onward.hrservice.entity.EmployeeBusinessDetails;
import com.onward.hrservice.entity.EmployeeEmergencyContactDetails;
import com.onward.hrservice.entity.EmployeeInfoView;
import com.onward.hrservice.entity.EmployeeMaster;
import com.onward.hrservice.entity.EmployeePassportDetails;
import com.onward.hrservice.entity.EmployeeVisaDetails;
import com.onward.hrservice.entity.OrganizationBranchCustomerDetails;
import com.onward.hrservice.entity.ReferenceMaster;
import com.onward.hrservice.exception.HrServiceException;
import com.onward.hrservice.repository.DocumentMasterRepository;
import com.onward.hrservice.repository.EmployeeAddressDetailsRepository;
import com.onward.hrservice.repository.EmployeeBankDetailsRepository;
import com.onward.hrservice.repository.EmployeeBusinessDetailsRepository;
import com.onward.hrservice.repository.EmployeeDetailsViewRepository;
import com.onward.hrservice.repository.EmployeeEmergencyContactDetailsRepository;
import com.onward.hrservice.repository.EmployeeInfoViewRepository;
import com.onward.hrservice.repository.EmployeeMasterRepository;
import com.onward.hrservice.repository.EmployeePassportDetailsRepository;
import com.onward.hrservice.repository.EmployeeVisaDetailsRepository;
import com.onward.hrservice.repository.OrganizationBranchCustomerDetailsRepository;
import com.onward.hrservice.repository.ReferenceMasterRepository;
import com.onward.hrservice.service.HrConnectService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HrConnectServiceImpl implements HrConnectService {

	@Value("${imagePath}")
	String imagePath;

	@Value("${documents}")
	String documents;

	@Autowired
	ReferenceMasterRepository referenceMasterRepo;

	@Autowired
	EmployeeMasterRepository employeeMasterRepo;

	@Autowired
	EmployeeAddressDetailsRepository employeeAddressRepo;

	@Autowired
	EmployeeBusinessDetailsRepository employeeBusinessRepo;

	@Autowired
	EmployeeInfoViewRepository employeeInfoRepo;

	@Autowired
	EmployeePassportDetailsRepository employeePassportRepo;

	@Autowired
	EmployeeVisaDetailsRepository employeeVisaRepo;

	@Autowired
	DocumentMasterRepository documentRepo;
	@Autowired
	EmployeeDetailsViewRepository empDetailsViewRepo;

	@Autowired
	EmployeeEmergencyContactDetailsRepository emergencyContactRepo;

	@Autowired
	OrganizationBranchCustomerDetailsRepository organizationRepo;
	@Autowired
	EmployeeBankDetailsRepository employeeBankDetailsRepo;

	private static final String PANCARD_PATH = "\\Pancard\\";
	private static final String PASSPORT_PATH = "\\Passport\\";
	private static final String AADHAR_PATH = "\\Aadhar\\";
	private static final String AADHAR = "aadhar";
	private static final String PANCARD = "pancard";
	private static final String PASSPORT = "passport";

	@Override
	public Map<String, Object> getDashboardList() {
		log.info("Get Dashboard Data:");
		List<EmployeeDashboardDto> employeeDashboardList = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		List<EmployeeMaster> employeeMasters = employeeMasterRepo.findAllDetails();
		for (EmployeeMaster employeeMaster : employeeMasters) {
			EmployeeDashboardDto employeeDashboard = new EmployeeDashboardDto();
			EmployeeBusinessDetails businessDetails = employeeBusinessRepo.findByEmpCode(employeeMaster.getEmpCode());
			EmployeeInfoView employeeInfoView = employeeInfoRepo.findByEmpCode(employeeMaster.getEmpCode());
			employeeDashboard.setEmployeeCode(employeeMaster.getEmpNumber());
			employeeDashboard.setEmployeeName(employeeMaster.getEmpName());
			employeeDashboard.setEmployeeDesignation(referenceMasterRepo.getRefDescription(employeeMaster.getEmpDesignation()));
			employeeDashboard.setGrade(employeeMaster.getEmpGrade());
			employeeDashboard.setCategory(referenceMasterRepo.getRefDescription(employeeMaster.getEmpCategory()));
			employeeDashboard.setHrbp(employeeMasterRepo.getHrbpName(employeeMaster.getEmpHrbpid()));
			employeeDashboard.setReportingManager(employeeMaster.getEmpRepManager());
			employeeDashboard.setProfileCompletion(employeeInfoView.getEnteredPer().substring(0, employeeInfoView.getEnteredPer().indexOf(".")));
			employeeDashboard.setEmployeeId(employeeMaster.getEmpCode().toString());
			if(employeeMaster.getEmpDob()!=null) {
				employeeDashboard.setEmpDob(employeeMaster.getEmpDob().format(formatter));		
			}	
			
			byte[] fileContent = null;
			try {
				fileContent = FileUtils.readFileToByteArray(new File(imagePath + "\\" + employeeMaster.getEmpNumber() + ".jpeg"));
			} catch (IOException e) {
				log.info("Exception in Employee images ::{}", e.getMessage());
			}
			if (fileContent != null) {
				employeeDashboard.setEmployeeImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent));
			}
			if (businessDetails != null) {
				employeeDashboard.setCustomer(businessDetails.getEmpbCusName());
			}
			employeeDashboardList.add(employeeDashboard);
		}
		log.info("Dashboard List Response ::{}", employeeDashboardList);
		return Utility.success(employeeDashboardList);
	}

	@Override
	public Map<String, Object> getMasterData() {
		log.info("Get Master Drop Down Data");
		Map<String, Object> map = new HashMap<>();
		List<MasterDataDto> statusMasterData = getAllMasterData(DropDownMaster.STATUS.getId());
		map.put("status", statusMasterData);
		List<MasterDataDto> businessUnitsData = getAllMasterData(DropDownMaster.BUSINESS_UNIT.getId());
		map.put("businessUnits", businessUnitsData);
		List<MasterDataDto> regionalBusinessUnitsData = getAllMasterData(DropDownMaster.REGIONAL_BUSINESS_UNIT.getId());
		map.put("regionalBusinessUnits", regionalBusinessUnitsData);
		List<MasterDataDto> customerNameData = getAllMasterData(DropDownMaster.CUSTOMER_NAME.getId());
		map.put("customerName", customerNameData);
		List<MasterDataDto> genderData = getAllMasterData(DropDownMaster.GENDER.getId());
		map.put("gender", genderData);
		List<MasterDataDto> maritalStatusData = getAllMasterData(DropDownMaster.MARITAL_STATUS.getId());
		map.put("maritalStatus", maritalStatusData);
		List<MasterDataDto> costAllocationData = getAllMasterData(DropDownMaster.COST_ALLOCATION.getId());
		map.put("costAllocation", costAllocationData);
		List<MasterDataDto> costEntityData = getAllMasterData(DropDownMaster.COST_ENTITY.getId());
		map.put("costEntity", costEntityData);
		List<MasterDataDto> businessFunctionData = getAllMasterData(DropDownMaster.BUSINESS_FUNCTION.getId());
		map.put("businessFunction", businessFunctionData);
		List<MasterDataDto> departmentData = getAllMasterData(DropDownMaster.DEPARTMENT.getId());
		map.put("department", departmentData);
		List<MasterDataDto> engagementModelData = getAllMasterData(DropDownMaster.ENGAGEMENT_MODEL.getId());
		map.put("engagementModel", engagementModelData);
		List<MasterDataDto> revenueCategoryData = getAllMasterData(DropDownMaster.REVENUE_CATEGORY.getId());
		map.put("revenueCategory", revenueCategoryData);
		List<MasterDataDto> lineOfBusinessData = getAllMasterData(DropDownMaster.LINE_OF_BUSINESS.getId());
		map.put("lineOfBusiness", lineOfBusinessData);
		List<MasterDataDto> designationData = getAllMasterData(DropDownMaster.DESIGNATION.getId());
		map.put("designation", designationData);
		List<MasterDataDto> confirmationStatusData = getAllMasterData(DropDownMaster.CONFIRMATION_STATUS.getId());
		map.put("confirmationStatus", confirmationStatusData);
		List<MasterDataDto> bondData = getAllMasterData(DropDownMaster.BOND.getId());
		map.put("bond", bondData);
		List<MasterDataDto> gradeData = getAllMasterData(DropDownMaster.GRADE.getId());
		map.put("grade", gradeData);
		List<MasterDataDto> categoryData = getAllMasterData(DropDownMaster.CATEGORY.getId());
		map.put("category", categoryData);
		List<MasterDataDto> bloodGroupData = getAllMasterData(DropDownMaster.BLOOD_GROUP.getId());
		map.put("bloodGroup", bloodGroupData);
		List<MasterDataDto> fnfData = getAllMasterData(DropDownMaster.FNF.getId());
		map.put("fnf", fnfData);
		List<MasterDataDto> baseLocationData = getAllMasterData(DropDownMaster.BASE_LOCAION.getId());
		map.put("baseLocation", baseLocationData);
		List<MasterDataDto> industryGroupData = getAllMasterData(DropDownMaster.INDUSTRY_GROUP.getId());
		map.put("industryGroup", industryGroupData);
		List<MasterDataDto> subIndustryData = getAllMasterData(DropDownMaster.SUB_INDUSTRY.getId());
		map.put("subIndustry", subIndustryData);
		List<MasterDataDto> subLobData = getAllMasterData(DropDownMaster.SUB_LOB.getId());
		map.put("subLob", subLobData);
		List<MasterDataDto> deputationData = getAllMasterData(DropDownMaster.DEPUTATION.getId());
		map.put("deputation", deputationData);
		List<MasterDataDto> noticePeriodData = getAllMasterData(DropDownMaster.NOTICE_PERIOD.getId());
		map.put("noticePeriod", noticePeriodData);
		List<MasterDataDto> pdCodeData = getAllMasterData(DropDownMaster.PD_CODE.getId());
		map.put("pdCode", pdCodeData);
		List<MasterDataDto> intraCompanyTransferData = getAllMasterData(DropDownMaster.INTRA_COMPANY_TRANSFER.getId());
		map.put("intraCompanyTransfer", intraCompanyTransferData);
		List<MasterDataDto> qualificationData = getAllMasterData(DropDownMaster.QUALIFICATION.getId());
		map.put("qualification", qualificationData);
		List<EmployeeBusinessDetails> businessDetails = employeeBusinessRepo.getHrpB(DropDownMaster.HRPB.getId());
		List<Integer> employeeCode = businessDetails.stream().map(EmployeeBusinessDetails::getEmpCode).collect(Collectors.toList());
		log.info("List of Employee with HRBP ::{}", employeeCode);
		List<EmployeeMaster> hrpb = employeeMasterRepo.getHrpb(employeeCode);
		List<MasterDataDto> hrpbData = getHrpbId(hrpb);
		map.put("hrbp", hrpbData);
		log.info("Master drop data Response ::{}", map);
		return Utility.success(map);
	}

	private List<MasterDataDto> getHrpbId(List<EmployeeMaster> hrpbs) {
		List<MasterDataDto> masterData = new ArrayList<>();
		hrpbs.stream().forEach(hr -> {
			MasterDataDto master = new MasterDataDto();
			master.setCode(hr.getEmpNumber());
			master.setDescription(hr.getEmpName());
			masterData.add(master);
		});
		return masterData;
	}

	private List<MasterDataDto> getAllMasterData(String status) {
		List<MasterDataDto> masterData = new ArrayList<>();
		List<ReferenceMaster> referenceMasters = referenceMasterRepo.findByRefParentCodeOrderByRefDescriptionAsc(status);
		referenceMasters.stream().forEach(refrence -> {
			MasterDataDto master = new MasterDataDto();
			master.setCode(refrence.getRefCode());
			master.setDescription(refrence.getRefDescription());
			masterData.add(master);
		});
		return masterData;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> saveEmployee(NewEmployeeDetailsDto data, String encodedUserId)
			throws HrServiceException {
		log.info("Save Employee Data");
//		EmployeeMaster getEmployee = employeeMasterRepo.findByEmpCode(Integer.parseInt(data.getBasicDetails().getEmployeeId()));
//		if (getEmployee != null) {
//			return Utility.fail("Details already present for this Employee Number");
//		}
		log.info("Encoded user id ::{}", encodedUserId);
		String userId = Utility.decode(encodedUserId);
		log.info("UserID ::{}", userId);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		EmployeeMaster employeeMaster = modelMapper.map(data.getBasicDetails(), EmployeeMaster.class);
		EmployeeAddressDetails employeeAddressDetails = modelMapper.map(data.getAddressDetails(),EmployeeAddressDetails.class);
		EmployeeBusinessDetails employeeBusinessDetails = modelMapper.map(data.getBusinsessDetails(),EmployeeBusinessDetails.class);
		EmployeePassportDetails employeePassportDetails = modelMapper.map(data.getPassportDetails(),EmployeePassportDetails.class);
		EmployeeVisaDetails employeeVisaDetails = modelMapper.map(data.getVisaDetails(), EmployeeVisaDetails.class);
		EmployeeEmergencyContactDetails employeeEmergencyContatcDetails = modelMapper.map(data.getEmergencyContactDetails(), EmployeeEmergencyContactDetails.class);
		EmployeeBankDetails employeeBankDetails = modelMapper.map(data.getBankDetails(), EmployeeBankDetails.class);
		if (data.getBasicDetails().getEmpRmid() != null) {
			EmployeeMaster reportingManager = employeeMasterRepo
					.findByEmpNumber(data.getBasicDetails().getEmpRmid().toString());
			if (reportingManager != null) {
				employeeMaster.setEmpRepManager(reportingManager.getEmpName());
			}
		}
		employeeMaster.setEmpDob(Utility.getDateFormat(data.getBasicDetails().getEmpDob()));
		employeeMaster.setEmpDoj(Utility.getDateFormat(data.getBasicDetails().getEmpDoj()));
		employeeMaster.setEmpInitialDoj(Utility.getDateFormat(data.getBasicDetails().getEmpInitialDoj()));
		employeeMaster.setEmpConformationDt(Utility.getDateFormat(data.getBasicDetails().getEmpConformationDt()));
		employeeMaster.setEmpBondStartDate(Utility.getDateFormat(data.getBasicDetails().getEmpBondStartDate()));
		employeeMaster.setEmpBondEndDate(Utility.getDateFormat(data.getBasicDetails().getEmpBondEndDate()));
		employeeMaster.setEmpActStartDate(Utility.getDateFormat(data.getBasicDetails().getEmpActStartDate()));
		employeeMaster.setEmpActEndDate(Utility.getDateFormat(data.getBasicDetails().getEmpActdEndDate()));
		employeeMaster.setEmpResDate(Utility.getDateFormat(data.getBasicDetails().getEmpResDate()));
		employeeMaster.setEmpTenLwd(Utility.getDateFormat(data.getBasicDetails().getEmpTenLwd()));
		employeeMaster.setEmpLwd(Utility.getDateFormat(data.getBasicDetails().getEmpLwd()));

		employeeMaster.setStatus("A");
		employeeMaster.setCreatedBy(Integer.parseInt(userId));
		employeeMaster.setCreatedOn(LocalDateTime.now());
		log.info("Employee Master Data saved to DB ::{}", employeeMaster);
		try {
			employeeMasterRepo.save(employeeMaster);
		} catch (Exception e) {
			log.info("Exception in Employee Master Details ::{}", e.getMessage());
		}
		EmployeeMaster employeeDetail = employeeMasterRepo.getLastRecord();
		employeeAddressDetails.setEmpCode(employeeDetail.getEmpCode());
		employeeAddressDetails.setCreatedBy(Integer.parseInt(userId));
		employeeAddressDetails.setCreatedOn(LocalDateTime.now());
		employeeAddressDetails.setStatus("A");
		log.info("Employee Address Data saved to DB ::{}", employeeAddressDetails);
		try {
			employeeAddressRepo.save(employeeAddressDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Address Details ::{}", e.getMessage());
		}
		employeeBusinessDetails.setEmbTfDepuDate(Utility.getDateFormat(data.getBusinsessDetails().getEmbTfDepuDate()));
		employeeBusinessDetails.setEmbTfDepuEndDate(Utility.getDateFormat(data.getBusinsessDetails().getEmbTfDepuEndDate()));
		employeeBusinessDetails.setEmpCode(employeeDetail.getEmpCode());
		employeeBusinessDetails.setStatus("A");
		employeeBusinessDetails.setCreatedBy(Integer.parseInt(userId));
		employeeBusinessDetails.setCreatedOn(LocalDateTime.now());
		log.info("Employee Business Data saved to DB ::{}", employeeBusinessDetails);
		try {
			employeeBusinessRepo.save(employeeBusinessDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Business Details ::{}", e.getMessage());
		}
		employeePassportDetails.setEmppIssueDate(Utility.getDateFormat(data.getPassportDetails().getEmppIssueDate()));
		employeePassportDetails.setEmppExpiryDate(Utility.getDateFormat(data.getPassportDetails().getEmppExpiryDate()));

		employeePassportDetails.setEmpCode(employeeDetail.getEmpCode());
		employeePassportDetails.setStatus("A");
		employeePassportDetails.setCreatedBy(Integer.parseInt(userId));
		employeePassportDetails.setCreatedOn(LocalDateTime.now());

		employeeVisaDetails.setEmpvStartDate(Utility.getDateFormat(data.getVisaDetails().getEmpvStartDate()));
		employeeVisaDetails.setEmpvEndDate(Utility.getDateFormat(data.getVisaDetails().getEmpvEndDate()));

		employeeVisaDetails.setEmpCode(employeeDetail.getEmpCode());
		employeeVisaDetails.setStatus("A");
		employeeVisaDetails.setCreatedBy(Integer.parseInt(userId));
		employeeVisaDetails.setCreatedOn(LocalDateTime.now());
		try {
			employeeVisaRepo.save(employeeVisaDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Visa Details ::{}", e.getMessage());
		}
		employeeEmergencyContatcDetails.setEmpCode(employeeDetail.getEmpCode());
		employeeEmergencyContatcDetails.setStatus("A");
		employeeEmergencyContatcDetails.setCreatedBy(Integer.parseInt(userId));
		employeeEmergencyContatcDetails.setCreatedOn(LocalDateTime.now());
		try {
			emergencyContactRepo.save(employeeEmergencyContatcDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Emergency Contact Details ::{}", e.getMessage());
		}
		employeeBankDetails.setEmpCode(employeeDetail.getEmpCode());
		employeeBankDetails.setStatus("A");
		employeeBankDetails.setCreatedBy(Integer.parseInt(userId));
		employeeBankDetails.setCreatedOn(LocalDateTime.now());

		try {
			employeeBankDetailsRepo.save(employeeBankDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Bank Details ::{}", e.getMessage());
		}
		if (data.getBasicDetails().getImage() != null) {
			saveImageFile(data);
		}
		if (data.getBasicDetails().getEmpAadharfileName() != null || data.getBasicDetails().getEmpPanfileName() != null
				|| data.getPassportDetails().getEmpPassportfileName() != null) {
			if (data.getBasicDetails().getEmpAadharfileName() != null) {
				DocumentMaster documentMaster = new DocumentMaster();
				documentMaster.setDocType(AADHAR);
				documentMaster.setDocName(data.getBasicDetails().getEmpAadharfileName());
				documentMaster.setDocLocation(documents + AADHAR_PATH);
				documentMaster.setEmpNumber(employeeDetail.getEmpCode().toString());
				documentMaster.setStatus("A");
				documentMaster.setCreatedBy(Integer.parseInt(userId));
				documentMaster.setCreatedOn(LocalDateTime.now());
				documentRepo.save(documentMaster);
			}
			if (data.getBasicDetails().getEmpPanfileName() != null) {
				DocumentMaster documentMaster = new DocumentMaster();
				documentMaster.setDocType(PANCARD);
				documentMaster.setDocName(data.getBasicDetails().getEmpPanfileName());
				documentMaster.setDocLocation(documents + PANCARD_PATH);
				documentMaster.setEmpNumber(employeeDetail.getEmpCode().toString());
				documentMaster.setStatus("A");
				documentMaster.setCreatedBy(Integer.parseInt(userId));
				documentMaster.setCreatedOn(LocalDateTime.now());
				documentRepo.save(documentMaster);
			}
			if (data.getPassportDetails().getEmpPassportfileName() != null) {
				DocumentMaster documentMaster = new DocumentMaster();
				documentMaster.setDocType(PASSPORT);
				documentMaster.setDocName(data.getPassportDetails().getEmpPassportfileName());
				documentMaster.setDocLocation(documents + PASSPORT_PATH);
				documentMaster.setEmpNumber(employeeDetail.getEmpCode().toString());
				documentMaster.setStatus("A");
				documentMaster.setCreatedBy(Integer.parseInt(userId));
				documentMaster.setCreatedOn(LocalDateTime.now());
				documentRepo.save(documentMaster);
			}
			List<DocumentMaster> docuMasters = documentRepo.findByEmpNumber(employeeDetail.getEmpCode().toString());
			for (DocumentMaster docuMaster : docuMasters) {
				if (docuMaster.getDocType().equalsIgnoreCase(AADHAR)) {
					employeeDetail.setEmpAadharDocNumber(docuMaster.getDocCode());
				} else if (docuMaster.getDocType().equalsIgnoreCase(PANCARD)) {
					employeeDetail.setEmpPanDocNumber(docuMaster.getDocCode());
				} else if (docuMaster.getDocType().equalsIgnoreCase(PASSPORT)) {
					employeePassportDetails.setEmppDocNumber(docuMaster.getDocCode());
				}
			}
			employeeMasterRepo.save(employeeDetail);
		}
		try {
			employeePassportRepo.save(employeePassportDetails);
		} catch (Exception e) {
			log.info("Exception in Employee Passport Details ::{}", e.getMessage());
		}
		return Utility.success("Employee Data Saved Successfully");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> updateEmployee(NewEmployeeDetailsDto data, String encodedUserId)
			throws HrServiceException {
		log.info("Update Employee Data");
		EmployeeMaster employeeMasterData = employeeMasterRepo
				.findByEmpCode(Integer.parseInt(data.getBasicDetails().getEmployeeId()));
		log.info("Employee Data from DB ::{}", employeeMasterData);
		if (employeeMasterData != null) {
			String userId = Utility.decode(encodedUserId);
			log.info("UserID ::{}", userId);
			EmployeeBusinessDetails businessDetails = employeeBusinessRepo.findByEmpCode(employeeMasterData.getEmpCode());
			EmployeeAddressDetails addressDetails = employeeAddressRepo.findByEmpCode(employeeMasterData.getEmpCode());
			EmployeePassportDetails passportDetails = employeePassportRepo.findByEmpCode(employeeMasterData.getEmpCode());
			EmployeeVisaDetails visaDetails = employeeVisaRepo.findByEmpCode(employeeMasterData.getEmpCode());
			EmployeeEmergencyContactDetails emergencyContactDetails = emergencyContactRepo.findByEmpCode(employeeMasterData.getEmpCode());
			EmployeeBankDetails employeeBankData = employeeBankDetailsRepo.findByEmpCode(employeeMasterData.getEmpCode());
			employeeMasterData.setEmpNumber(data.getBasicDetails().getEmpNumber());
			employeeMasterData.setEmpAadharNumber(data.getBasicDetails().getEmpAadharNumber());
			employeeMasterData.setEmpAmountOfBond(data.getBasicDetails().getEmpAmountOfBond());
			employeeMasterData.setEmpBloodGroup(data.getBasicDetails().getEmpBloodGroup());
			employeeMasterData.setEmpBondApplicable(data.getBasicDetails().getEmpBondApplicable());
			employeeMasterData.setEmpCategory(data.getBasicDetails().getEmpCategory());
			employeeMasterData.setEmpCertification(data.getBasicDetails().getEmpCertification());
			employeeMasterData.setEmpConformationStatus(data.getBasicDetails().getEmpConformationStatus());
			employeeMasterData.setEmpDesignation(data.getBasicDetails().getEmpDesignation());
			employeeMasterData.setEmpEpfNumber(data.getBasicDetails().getEmpEpfNumber());
			employeeMasterData.setEmpEsicNumber(data.getBasicDetails().getEmpEsicNumber());
			employeeMasterData.setEmpFatherOrHusbandName(data.getBasicDetails().getEmpFatherOrHusbandName());
			employeeMasterData.setEmpFnf(data.getBasicDetails().getEmpFnf());
			employeeMasterData.setEmpGender(data.getBasicDetails().getEmpGender());
			employeeMasterData.setEmpGrade(data.getBasicDetails().getEmpGrade());
			employeeMasterData.setEmpGratFormula(data.getBasicDetails().getEmpGratFormula());
			employeeMasterData.setEmpHrbpid(data.getBasicDetails().getEmpHrbpid());
			employeeMasterData.setEmpInitialOtlExp(data.getBasicDetails().getEmpInitialOtlExp());
			employeeMasterData.setEmpLwdMonth(data.getBasicDetails().getEmpLwdMonth());
			employeeMasterData.setEmpMaritalStatus(data.getBasicDetails().getEmpMaritalStatus());
			employeeMasterData.setEmpName(data.getBasicDetails().getEmpName());
			employeeMasterData.setEmpNoticePeriod(data.getBasicDetails().getEmpNoticePeriod());
			employeeMasterData.setEmpPanNumber(data.getBasicDetails().getEmpPanNumber());
			employeeMasterData.setEmpPersonalEmailId(data.getBasicDetails().getEmpPersonalEmailId());
			employeeMasterData.setEmpPreviousExp(data.getBasicDetails().getEmpPreviousExp());
			employeeMasterData.setEmpPrimContactNo(data.getBasicDetails().getEmpPrimContactNo());
			employeeMasterData.setEmpQualification(data.getBasicDetails().getEmpQualification());
			employeeMasterData.setEmpReason(data.getBasicDetails().getEmpReason());
			employeeMasterData.setEmpRelevantExpBj(data.getBasicDetails().getEmpRelevantExpBj());
			employeeMasterData.setEmpRmid(data.getBasicDetails().getEmpRmid());
			employeeMasterData.setEmpSecContactNo(data.getBasicDetails().getEmpSecContactNo());
			employeeMasterData.setEmpSkillSet(data.getBasicDetails().getEmpSkillSet());
			employeeMasterData.setEmpSpecial(data.getBasicDetails().getEmpSpecial());
			employeeMasterData.setEmpState(data.getBasicDetails().getEmpState());
			employeeMasterData.setEmpUanNumber(data.getBasicDetails().getEmpUanNumber());
			employeeMasterData.setEmpDeputationApplicable(data.getBasicDetails().getEmpDeputationApplicable());
			employeeMasterData.setEmpEmailAddress(data.getBasicDetails().getEmpEmailAddress());
			if (data.getBasicDetails().getEmpRmid() != null) {
				EmployeeMaster reportingManager = employeeMasterRepo.findByEmpNumber(data.getBasicDetails().getEmpRmid());
				employeeMasterData.setEmpRepManager(reportingManager.getEmpName());
			}
			employeeMasterData.setEmpDob(Utility.getDateFormat(data.getBasicDetails().getEmpDob()));
			employeeMasterData.setEmpDoj(Utility.getDateFormat(data.getBasicDetails().getEmpDoj()));
			employeeMasterData.setEmpInitialDoj(Utility.getDateFormat(data.getBasicDetails().getEmpInitialDoj()));
			employeeMasterData.setEmpConformationDt(Utility.getDateFormat(data.getBasicDetails().getEmpConformationDt()));
			employeeMasterData.setEmpBondStartDate(Utility.getDateFormat(data.getBasicDetails().getEmpBondStartDate()));
			employeeMasterData.setEmpBondEndDate(Utility.getDateFormat(data.getBasicDetails().getEmpBondEndDate()));
			employeeMasterData.setEmpActStartDate(Utility.getDateFormat(data.getBasicDetails().getEmpActStartDate()));
			employeeMasterData.setEmpActEndDate(Utility.getDateFormat(data.getBasicDetails().getEmpActdEndDate()));
			employeeMasterData.setEmpResDate(Utility.getDateFormat(data.getBasicDetails().getEmpResDate()));
			employeeMasterData.setEmpTenLwd(Utility.getDateFormat(data.getBasicDetails().getEmpTenLwd()));
			employeeMasterData.setEmpLwd(Utility.getDateFormat(data.getBasicDetails().getEmpLwd()));
			employeeMasterData.setOrgbCode(data.getBasicDetails().getOrgbCode());

			employeeMasterData.setStatus("A");
			employeeMasterData.setEmpStatus(data.getBasicDetails().getEmpStatus());
			employeeMasterData.setModifiedBy(Integer.parseInt(userId));
			employeeMasterData.setModifiedOn(LocalDateTime.now());

			addressDetails.setEmpaCurAddress1(data.getAddressDetails().getEmpaCurAddress1());
			addressDetails.setEmpaCurAddress2(data.getAddressDetails().getEmpaCurAddress2());
			addressDetails.setEmpaCurAddress3(data.getAddressDetails().getEmpaCurAddress3());
			addressDetails.setEmpaCurPincode(data.getAddressDetails().getEmpaCurPincode());
			addressDetails.setEmpaPerAddress1(data.getAddressDetails().getEmpaPerAddress1());
			addressDetails.setEmpaPerAddress2(data.getAddressDetails().getEmpaPerAddress2());
			addressDetails.setEmpaPerAddress3(data.getAddressDetails().getEmpaPerAddress3());
			addressDetails.setEmpaPerPincode(data.getAddressDetails().getEmpaPerPincode());
			addressDetails.setStatus("A");
			addressDetails.setModifiedBy(Integer.parseInt(userId));
			addressDetails.setModifiedOn(LocalDateTime.now());
			log.info("Update Employee Address Data in DB ::{}", addressDetails);
			employeeAddressRepo.save(addressDetails);
			businessDetails.setEmpbBu(data.getBusinsessDetails().getEmpbBu());
			businessDetails.setEmpbCostAllocation(data.getBusinsessDetails().getEmpbCostAllocation());
			businessDetails.setEmpbCostEntity(data.getBusinsessDetails().getEmpbCostEntity());
			businessDetails.setEmpbCusName(data.getBusinsessDetails().getEmpbCusName());
			businessDetails.setEmpbCustCode(data.getBusinsessDetails().getEmpbCustCode());
			businessDetails.setEmpbCustCountry(data.getBusinsessDetails().getEmpbCustCountry());
			businessDetails.setEmpbCustState(data.getBusinsessDetails().getEmpbCustState());
			businessDetails.setEmpbDepEngagement(data.getBusinsessDetails().getEmpbDepEngagement());
			businessDetails.setEmpbDepId(data.getBusinsessDetails().getEmpbDepId());
			businessDetails.setEmpbLob(data.getBusinsessDetails().getEmpbLob());
			businessDetails.setEmpbLobRevenueCat(data.getBusinsessDetails().getEmpbLobRevenueCat());
			businessDetails.setEmpbPdCode(data.getBusinsessDetails().getEmpbPdCode());
			businessDetails.setEmpbRegionalBu(data.getBusinsessDetails().getEmpbRegionalBu());
			businessDetails.setEmpbCostBusFunction(data.getBusinsessDetails().getEmpbCostBusFunction());
			businessDetails.setEmpbDepIndustry(data.getBusinsessDetails().getEmpbDepIndustry());
			businessDetails.setEmpbDepSubindustry(data.getBusinsessDetails().getEmpbDepSubindustry());
			businessDetails.setEmpbDepBaselocation(data.getBusinsessDetails().getEmpbDepBaselocation());
			businessDetails.setEmpbLobSub(data.getBusinsessDetails().getEmpbLobSub());
			businessDetails.setEmbTfIsintratransfer(data.getBusinsessDetails().getEmbTfIsintratransfer());
			businessDetails.setEmpbTfWorklocation(data.getBusinsessDetails().getEmpbTfWorklocation());
			businessDetails.setEmpbTfState(data.getBusinsessDetails().getEmpbTfState());
			businessDetails.setEmpbTfCost(data.getBusinsessDetails().getEmpbTfCost());
			businessDetails.setEmbTfDepuDate(Utility.getDateFormat(data.getBusinsessDetails().getEmbTfDepuDate()));
			businessDetails.setEmbTfDepuEndDate(Utility.getDateFormat(data.getBusinsessDetails().getEmbTfDepuEndDate()));
			businessDetails.setStatus("A");
			businessDetails.setModifiedBy(Integer.parseInt(userId));
			businessDetails.setModifiedOn(LocalDateTime.now());
			log.info("Update Employee Business Data in DB ::{}", businessDetails);
			employeeBusinessRepo.save(businessDetails);
			passportDetails.setEmppNumber(data.getPassportDetails().getEmppNumber());
			passportDetails.setEmppCountry(data.getPassportDetails().getEmppCountry());
			passportDetails.setEmppIssueDate(Utility.getDateFormat(data.getPassportDetails().getEmppIssueDate()));
			passportDetails.setEmppExpiryDate(Utility.getDateFormat(data.getPassportDetails().getEmppExpiryDate()));

			passportDetails.setStatus("A");
			passportDetails.setModifiedBy(Integer.parseInt(userId));
			passportDetails.setModifiedOn(LocalDateTime.now());

			visaDetails.setEmpvVisaType(data.getVisaDetails().getEmpvVisaType());
			visaDetails.setEmpvStartDate(Utility.getDateFormat(data.getVisaDetails().getEmpvStartDate()));
			visaDetails.setEmpvEndDate(Utility.getDateFormat(data.getVisaDetails().getEmpvEndDate()));

			visaDetails.setStatus("A");
			visaDetails.setModifiedBy(Integer.parseInt(userId));
			visaDetails.setModifiedOn(LocalDateTime.now());
			employeeVisaRepo.save(visaDetails);
			emergencyContactDetails.setEmgcContactNumber(data.getEmergencyContactDetails().getEmgcContactNumber());
			emergencyContactDetails.setEmgcName(data.getEmergencyContactDetails().getEmgcName());
			emergencyContactDetails.setEmgcRelationship(data.getEmergencyContactDetails().getEmgcRelationship());
			emergencyContactDetails.setStatus("A");
			emergencyContactDetails.setModifiedBy(Integer.parseInt(userId));
			emergencyContactDetails.setModifiedOn(LocalDateTime.now());
			emergencyContactRepo.save(emergencyContactDetails);
			employeeBankData.setEmbkBankAccNo(data.getBankDetails().getEmbkBankAccNo());
			employeeBankData.setEmbkBankName(data.getBankDetails().getEmbkBankName());
			employeeBankData.setEmbkIfscCode(data.getBankDetails().getEmbkIfscCode());
			employeeBankData.setEmbkMnhId(data.getBankDetails().getEmbkMnhId());
			employeeBankData.setEmbkNameAsperBank(data.getBankDetails().getEmbkNameAsperBank());
			employeeBankData.setStatus("A");
			employeeBankData.setModifiedBy(Integer.parseInt(userId));
			employeeBankData.setModifiedOn(LocalDateTime.now());
			employeeBankDetailsRepo.save(employeeBankData);
			DocumentMaster aadharDocument = documentRepo.getDocumentByType(data.getBasicDetails().getEmpNumber(),AADHAR);
			if (aadharDocument != null) {
				aadharDocument.setDocName(data.getBasicDetails().getEmpAadharfileName());
				aadharDocument.setModifiedBy(Integer.parseInt(userId));
				aadharDocument.setModifiedOn(LocalDateTime.now());
				documentRepo.save(aadharDocument);
			} else {
				DocumentMaster aadharDocu = new DocumentMaster();
				aadharDocu.setDocType(AADHAR);
				aadharDocu.setDocName(data.getBasicDetails().getEmpAadharfileName());
				aadharDocu.setDocLocation(documents + AADHAR_PATH);
				aadharDocu.setEmpNumber(employeeMasterData.getEmpCode().toString());
				aadharDocu.setCreatedBy(Integer.parseInt(userId));
				aadharDocu.setModifiedOn(LocalDateTime.now());
				documentRepo.save(aadharDocu);
			}
			DocumentMaster panDocument = documentRepo.getDocumentByType(data.getBasicDetails().getEmpNumber(), PANCARD);
			if (panDocument != null) {
				panDocument.setDocName(data.getBasicDetails().getEmpPanfileName());
				panDocument.setModifiedBy(Integer.parseInt(userId));
				panDocument.setModifiedOn(LocalDateTime.now());
				documentRepo.save(panDocument);
			} else {
				DocumentMaster panDocu = new DocumentMaster();
				panDocu.setDocType(PANCARD);
				panDocu.setDocName(data.getBasicDetails().getEmpPanfileName());
				panDocu.setDocLocation(documents + PANCARD_PATH);
				panDocu.setEmpNumber(employeeMasterData.getEmpCode().toString());
				panDocu.setCreatedBy(Integer.parseInt(userId));
				panDocu.setCreatedOn(LocalDateTime.now());
				documentRepo.save(panDocu);
			}
			DocumentMaster passportDocument = documentRepo.getDocumentByType(data.getBasicDetails().getEmpNumber(),PASSPORT);
			if (passportDocument != null) {
				passportDocument.setDocName(data.getPassportDetails().getEmpPassportfileName());
				passportDocument.setModifiedBy(Integer.parseInt(userId));
				passportDocument.setCreatedOn(LocalDateTime.now());
				documentRepo.save(passportDocument);
			} else {
				DocumentMaster passportDocu = new DocumentMaster();
				passportDocu.setDocType(PASSPORT);
				passportDocu.setDocName(data.getPassportDetails().getEmpPassportfileName());
				passportDocu.setDocLocation(documents + PASSPORT_PATH);
				passportDocu.setEmpNumber(employeeMasterData.getEmpCode().toString());
				passportDocu.setCreatedBy(Integer.parseInt(userId));
				passportDocu.setCreatedOn(LocalDateTime.now());
				documentRepo.save(passportDocu);
			}

			List<DocumentMaster> docuMasters = documentRepo.findByEmpNumber(employeeMasterData.getEmpCode().toString());
			for (DocumentMaster docuMaster : docuMasters) {
				if (docuMaster.getDocType().equalsIgnoreCase(AADHAR)) {
					employeeMasterData.setEmpAadharDocNumber(docuMaster.getDocCode());
				} else if (docuMaster.getDocType().equalsIgnoreCase(PANCARD)) {
					employeeMasterData.setEmpPanDocNumber(docuMaster.getDocCode());
				} else if (docuMaster.getDocType().equalsIgnoreCase(PASSPORT)) {
					passportDetails.setEmppDocNumber(docuMaster.getDocCode());
				}
			}
			log.info("Update Employee Master Data in DB ::{}", employeeMasterData);
			employeeMasterRepo.save(employeeMasterData);
			employeePassportRepo.save(passportDetails);
			if (data.getBasicDetails().getImage() != null) {
				saveImageFile(data);
			}
		} else {
			return Utility.fail("Employee Details not present");
		}

		return Utility.success("Employee Data Updated Successfully");
	}

	@Override
	public Map<String, Object> getEmployeeData(String data) {
		log.info("Get Employee Details based on Employee number");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		EmployeeMaster employeeMasterData = employeeMasterRepo.findByEmpCode(Integer.parseInt(data));
		EmployeeAddressDetails employeeAddressData = employeeAddressRepo.findByEmpCode(employeeMasterData.getEmpCode());
		EmployeeBusinessDetails employeeBusinessData = employeeBusinessRepo.findByEmpCode(employeeMasterData.getEmpCode());
		EmployeePassportDetails employeePassportData = employeePassportRepo.findByEmpCode(employeeMasterData.getEmpCode());
		EmployeeVisaDetails employeeVisaData = employeeVisaRepo.findByEmpCode(employeeMasterData.getEmpCode());
		EmployeeEmergencyContactDetails employeeEmergencyContactData = emergencyContactRepo.findByEmpCode(employeeMasterData.getEmpCode());
		EmployeeBankDetails employeeBankData = employeeBankDetailsRepo.findByEmpCode(employeeMasterData.getEmpCode());
		List<DocumentMaster> documnetMasters = documentRepo.findByEmpNumber(employeeMasterData.getEmpCode().toString());
		EmployeeInfoView employeeInfoView = employeeInfoRepo.findByEmpCode(employeeMasterData.getEmpCode());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		BasicDetailsDto employeeMaster = modelMapper.map(employeeMasterData, BasicDetailsDto.class);
		BusinessDetailsDto employeeBusiness = modelMapper.map(employeeBusinessData, BusinessDetailsDto.class);
		EmergencyContactDetailsDto employeeEmergencyContact = modelMapper.map(employeeEmergencyContactData,
				EmergencyContactDetailsDto.class);
		EmployeeBankDetailsDto employeeBankDetail = modelMapper.map(employeeBankData, EmployeeBankDetailsDto.class);
		readImageFile(employeeMaster);
		employeeMaster.setProfileCompletion(
				employeeInfoView.getEnteredPer().substring(0, employeeInfoView.getEnteredPer().indexOf(".")));
		if (employeeMasterData.getEmpRmid() != null) {
			EmployeeMaster reportingManager = employeeMasterRepo.findByEmpNumber(employeeMasterData.getEmpRmid().toString());
			employeeMaster.setEmpRepManager(reportingManager.getEmpName() + "(" + reportingManager.getEmpNumber() + ")");
		}
		if (employeeMasterData.getEmpDob() != null) {
			employeeMaster.setEmpDob(employeeMasterData.getEmpDob().format(formatter));
		}
		if (employeeMasterData.getEmpDoj() != null) {
			employeeMaster.setEmpDoj(employeeMasterData.getEmpDoj().format(formatter));
		}
		if (employeeMasterData.getEmpInitialDoj() != null) {
			employeeMaster.setEmpInitialDoj(employeeMasterData.getEmpInitialDoj().format(formatter));
		}
		if (employeeMasterData.getEmpConformationDt() != null) {
			employeeMaster.setEmpConformationDt(employeeMasterData.getEmpConformationDt().format(formatter));
		}
		if (employeeMasterData.getEmpBondStartDate() != null) {
			employeeMaster.setEmpBondStartDate(employeeMasterData.getEmpBondStartDate().format(formatter));
		}
		if (employeeMasterData.getEmpBondEndDate() != null) {
			employeeMaster.setEmpBondEndDate(employeeMasterData.getEmpBondEndDate().format(formatter));
		}
		if (employeeMasterData.getEmpActStartDate() != null) {
			employeeMaster.setEmpActStartDate(employeeMasterData.getEmpActStartDate().format(formatter));
		}
		if (employeeMasterData.getEmpActEndDate() != null) {
			employeeMaster.setEmpActdEndDate(employeeMasterData.getEmpActEndDate().format(formatter));
		}
		if (employeeMasterData.getEmpResDate() != null) {
			employeeMaster.setEmpResDate(employeeMasterData.getEmpResDate().format(formatter));
		}
		if (employeeMasterData.getEmpTenLwd() != null) {
			employeeMaster.setEmpTenLwd(employeeMasterData.getEmpTenLwd().format(formatter));
		}
		if (employeeMasterData.getEmpLwd() != null) {
			employeeMaster.setEmpLwd(employeeMasterData.getEmpLwd().format(formatter));
		}
		employeeMaster.setEmployeeId(employeeMasterData.getEmpCode().toString());
		if (employeeBusinessData.getEmbTfDepuDate() != null) {
			employeeBusiness.setEmbTfDepuDate(employeeBusinessData.getEmbTfDepuDate().format(formatter));
		}
		if (employeeBusinessData.getEmpbDepId() != null) {
			employeeBusiness.setEmpbDepName(referenceMasterRepo.getRefDescription(employeeBusinessData.getEmpbDepId()));
		}
		if (employeeBusinessData.getEmbTfDepuEndDate() != null) {
			employeeBusiness.setEmbTfDepuEndDate(employeeBusinessData.getEmbTfDepuEndDate().format(formatter));
		}

		if (employeeMasterData.getOrgbCode() != null) {
			OrganizationBranchCustomerDetails customerDetails = organizationRepo.findByOrgbcCode(employeeMasterData.getOrgbCode());
			employeeBusiness.setEmpbCusName(customerDetails.getOrgbcCustomer());
			employeeBusiness.setEmpbCustCode(customerDetails.getOrgbcCustomercode());
			employeeBusiness.setEmpbCustState(customerDetails.getOrgbcState());
			employeeBusiness.setEmpbCustCountry(customerDetails.getOrgbcCountry());
		}
		PassportDetailsDto employeePassport = null;
		if (employeePassportData != null) {
			employeePassport = modelMapper.map(employeePassportData, PassportDetailsDto.class);
			if (employeePassportData.getEmppIssueDate() != null) {
				employeePassport.setEmppIssueDate(employeePassportData.getEmppIssueDate().format(formatter));
			}
			if (employeePassportData.getEmppExpiryDate() != null) {
				employeePassport.setEmppExpiryDate(employeePassportData.getEmppExpiryDate().format(formatter));
			}
		}
		VisaDetailsDto employeeVisa = null;
		if (employeeVisaData != null) {
			employeeVisa = modelMapper.map(employeeVisaData, VisaDetailsDto.class);
			if (employeeVisaData.getEmpvStartDate() != null) {
				employeeVisa.setEmpvStartDate(employeeVisaData.getEmpvStartDate().format(formatter));
			}
			if (employeeVisaData.getEmpvEndDate() != null) {
				employeeVisa.setEmpvEndDate(employeeVisaData.getEmpvEndDate().format(formatter));
			}
		}
		for (DocumentMaster docMaster : documnetMasters) {
			if (docMaster.getDocType().equalsIgnoreCase(AADHAR)) {
				employeeMaster.setEmpAadharfileName(docMaster.getDocName());
				employeeMaster.setEmpAadharAttachment(exportAttachment(AADHAR, data));
			} else if (docMaster.getDocType().equalsIgnoreCase(PANCARD)) {
				employeeMaster.setEmpPanfileName(docMaster.getDocName());
				employeeMaster.setEmpAadharAttachment(exportAttachment(PANCARD, data));
			} else if (docMaster.getDocType().equalsIgnoreCase(PASSPORT)) {
				employeePassport.setEmpPassportfileName(docMaster.getDocName());
				employeePassport.setEmpPassportAttachment(exportAttachment(PASSPORT, data));
			}
		}
		commonCalculation(employeeMasterData, employeeMaster);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("basicDetails", employeeMaster);
		map.put("addressDetails", employeeAddressData);
		map.put("businsessDetails", employeeBusiness);
		map.put("passportDetails", employeePassport);
		map.put("visaDetails", employeeVisa);
		map.put("emergencyContactDetails", employeeEmergencyContact);
		map.put("bankDetails", employeeBankDetail);
		log.info("Employee Details Response ::{}", map);
		return Utility.success(map);
	}

	@Override
	public Map<String, Object> getReportingManager(String data) {
		log.info("Get List of Reporting Manager details");
		List<EmployeeMaster> reportingManager = employeeMasterRepo.getReportingManager(data);
		List<EmployeeDetailsDto> rmList = new ArrayList<>();
		reportingManager.stream().forEach(rm -> {
			EmployeeDetailsDto employee = new EmployeeDetailsDto();
			employee.setEmpName(rm.getEmpName() + "(" + rm.getEmpNumber() + ")");
			employee.setEmpNumber(rm.getEmpNumber());
			rmList.add(employee);
		});
		return Utility.success(rmList);
	}

	@Override
	public Map<String, Object> getDropDownMapping(String data) {
		log.info("Get List of Drop Down based on parent");
		List<MasterDataDto> masterData = new ArrayList<>();
		List<ReferenceMaster> mappingDropDowns = referenceMasterRepo.findByRefChildOf(data);
		mappingDropDowns.stream().forEach(mapping -> {
			MasterDataDto master = new MasterDataDto();
			master.setCode(mapping.getRefCode());
			master.setDescription(mapping.getRefDescription());
			masterData.add(master);
		});
		return Utility.success(masterData);
	}

	private void saveImageFile(NewEmployeeDetailsDto data) {
		try {
			Files.createDirectories(new File(imagePath).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] decodedImg = Base64.getDecoder().decode(data.getBasicDetails().getImage().replace("data:image/jpeg;base64,", "").getBytes(StandardCharsets.UTF_8));
		try (OutputStream stream = new FileOutputStream(imagePath + "//" + data.getBasicDetails().getEmpNumber() + ".jpeg")) {
			stream.write(decodedImg);
		} catch (Exception e) {
			log.info("Exception in Employee images ::{}", e.getMessage());
		}
	}
	private void commonCalculation(EmployeeMaster employeeMasterData, BasicDetailsDto employeeMaster) {
		LocalDate currentDate = LocalDate.now();
		if (employeeMasterData.getEmpDob() != null && currentDate != null) {
			employeeMaster
					.setEmpAge(Period.between(employeeMasterData.getEmpDob().toLocalDate(), currentDate).getYears());
		}
		if (employeeMasterData.getEmpDoj() != null && currentDate != null) {
			Period period = Period.between(employeeMasterData.getEmpDoj().toLocalDate(), currentDate);
			employeeMaster.setEmpOtlExp(period.getYears() + "." + period.getMonths());
		}
		if (employeeMasterData.getEmpPreviousExp() != null && employeeMaster.getEmpOtlExp() != null) {
			String previousYear = null;
			String previousMonth = null;
			String onwardYear = null;
			String onwardMonth = null;
			if (employeeMasterData.getEmpPreviousExp().contains(".")) {
				previousYear = employeeMasterData.getEmpPreviousExp().substring(0,
						employeeMasterData.getEmpPreviousExp().indexOf("."));
				previousMonth = employeeMasterData.getEmpPreviousExp().substring(2);
			} else {
				previousYear = employeeMasterData.getEmpPreviousExp();
				previousMonth = "0";
			}
			if (employeeMaster.getEmpOtlExp().contains(".")) {
				onwardYear = employeeMaster.getEmpOtlExp().substring(0, employeeMaster.getEmpOtlExp().indexOf("."));
				onwardMonth = employeeMaster.getEmpOtlExp().substring(2);
			} else {
				onwardYear = employeeMaster.getEmpOtlExp();
				onwardMonth = "0";
			}
			Integer resultMonth = Integer.parseInt(previousMonth) + Integer.parseInt(onwardMonth);
			int resultYear = 0;
			if (resultMonth >= 12) {
				resultMonth = resultMonth - 12;
				resultYear = 1;
			}
			resultYear = resultYear + Integer.parseInt(previousYear) + Integer.parseInt(onwardYear);
			String total = resultYear + "." + resultMonth;
			employeeMaster.setEmpTotalExp(total);
		}
	}

	private void readImageFile(BasicDetailsDto employeeMaster) {
		byte[] fileContent = null;
		try {
			fileContent = FileUtils
					.readFileToByteArray(new File(imagePath + "\\" + employeeMaster.getEmpNumber() + ".jpeg"));
		} catch (IOException e) {
			log.info("Exception in Employee images ::{}", e.getMessage());
		}
		if (fileContent != null) {
			employeeMaster.setImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent));
		}
	}

	@Override
	public Map<String, Object> saveAttachment(MultipartFile data, String type, String empNumber) {
		if (type.equalsIgnoreCase(AADHAR)) {
			saveDocuments(AADHAR_PATH, data, empNumber);
		} else if (type.equalsIgnoreCase(PANCARD)) {
			saveDocuments(PANCARD_PATH, data, empNumber);
		} else if (type.equalsIgnoreCase(PASSPORT)) {
			saveDocuments(PASSPORT_PATH, data, empNumber);
		}
		return Utility.success("Document Uploded Successfully");
	}

	@Override
	public Map<String, Object> deleteAttachment(String type, String empNumber) {
		if (type.equalsIgnoreCase(AADHAR)) {
			removeDocument(AADHAR_PATH, empNumber);
		} else if (type.equalsIgnoreCase(PANCARD)) {
			removeDocument(PANCARD_PATH, empNumber);
		} else if (type.equalsIgnoreCase(PASSPORT)) {
			removeDocument(PASSPORT_PATH, empNumber);
		}
		return Utility.success("Document Deleted Successfully");
	}

	@Override
	public byte[] exportAttachment(String type, String empNumber) {
		if (type.equalsIgnoreCase(AADHAR)) {
			return readDocument(AADHAR_PATH, empNumber);
		} else if (type.equalsIgnoreCase(PANCARD)) {
			return readDocument(PANCARD_PATH, empNumber);
		} else if (type.equalsIgnoreCase(PASSPORT)) {
			return readDocument(PASSPORT_PATH, empNumber);
		}
		return null;
	}

	private void saveDocuments(String path, MultipartFile data, String empNumber) {
		if (data != null) {
			String rootPath = documents + path;
			try {
				Files.createDirectories(new File(rootPath).toPath());
				String destination = rootPath + data.getOriginalFilename();
				String fileNameWithOutExt = FilenameUtils.removeExtension(data.getOriginalFilename());
				destination = destination.replace(fileNameWithOutExt, empNumber);
				File file = new File(destination);
				data.transferTo(file);
			} catch (Exception e) {
				log.info("Exception in Employee Document ::{}", e.getMessage());
			}
		}
	}

	private void removeDocument(String path, String empNumber) {
		String[] extensions = { "png", "jpg", "jpeg", "pdf" };
		try {
			List<String> files = findFiles(Paths.get(documents + path), extensions, empNumber);
			File file = new File(files.get(0));
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			log.info("Exception in Employee delete document ::{}", e.getMessage());
		}
	}

	private byte[] readDocument(String path, String empNumber) {
		String[] extensions = { "png", "jpg", "jpeg", "pdf" };
		byte[] fileContent = null;
		try {
			List<String> files = findFiles(Paths.get(documents + path), extensions, empNumber);
			File file = new File(files.get(0));
			if (file.exists()) {
				fileContent = FileUtils.readFileToByteArray(file);
			}
		} catch (Exception e) {
			log.info("Exception in Employee Aadhar read Document ::{}", e.getMessage());
		}
		return fileContent;
	}

	public List<String> findFiles(Path path, String[] fileExtensions, String empNumber) throws IOException {
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}
		List<String> result;
		try (Stream<Path> walk = Files.walk(path, 1)) {
			result = walk.filter(p -> !Files.isDirectory(p))
					// convert path to string
					.map(p -> p.toString().toLowerCase()).filter(f -> f.contains(empNumber))
					.collect(Collectors.toList());
		}
		return result;

	}

	@Override
	public Map<String, Object> getCustomerDetails(String customerName) {
		OrganizationBranchCustomerDetails customerDetails = organizationRepo.findByOrgbcCustomer(customerName);
		if (customerDetails != null) {
			log.info("Customer Details ::{}", customerDetails);
			Map<String, Object> map = new HashMap<>();
			map.put("orgbCode", customerDetails.getOrgbcCode());
			map.put("orgbcCustomercode", customerDetails.getOrgbcCustomercode());
			map.put("orgbcState", customerDetails.getOrgbcState());
			map.put("orgbcCountry", customerDetails.getOrgbcCountry());
			return Utility.success(map);
		}
		return Utility.fail("Customer Details not Present");
	}

	@Override
	public Map<String, Object> getEmployeeDetailsWithFilterDoj(EmployeeDetailsDto data) {
		log.info("Get Dashboard Data:");
		List<EmployeeDashboardDto> employeeDashboardList = new ArrayList<>();
		log.info("FromDate : " + Utility.getLocalDateFormat(data.getFromDate()));
		log.info("ToDate : " + Utility.getLocalDateFormat(data.getToDate()));

		List<EmployeeMaster> employeeDetails = employeeMasterRepo.findByDoj1(Utility.getDateFormat(data.getFromDate()),
				Utility.getDateFormat(data.getToDate()));
		if (!employeeDetails.isEmpty()) {
			for (EmployeeMaster employeeMaster : employeeDetails) {
				EmployeeDashboardDto employeeDashboard = new EmployeeDashboardDto();
				EmployeeBusinessDetails businessDetails = employeeBusinessRepo
						.findByEmpCode(employeeMaster.getEmpCode());
				EmployeeInfoView employeeInfoView = employeeInfoRepo.findByEmpCode(employeeMaster.getEmpCode());
				employeeDashboard.setEmployeeCode(employeeMaster.getEmpNumber());
				employeeDashboard.setEmployeeName(employeeMaster.getEmpName());
				employeeDashboard.setEmployeeDesignation(
						referenceMasterRepo.getRefDescription(employeeMaster.getEmpDesignation()));
				employeeDashboard.setGrade(employeeMaster.getEmpGrade());
				employeeDashboard.setCategory(referenceMasterRepo.getRefDescription(employeeMaster.getEmpCategory()));
				employeeDashboard.setHrbp(employeeMasterRepo.getHrbpName(employeeMaster.getEmpHrbpid()));
				employeeDashboard.setReportingManager(employeeMaster.getEmpRepManager());
				employeeDashboard.setProfileCompletion(
						employeeInfoView.getEnteredPer().substring(0, employeeInfoView.getEnteredPer().indexOf(".")));
				employeeDashboard.setEmployeeId(employeeMaster.getEmpCode().toString());
				byte[] fileContent = null;
				try {
					fileContent = FileUtils
							.readFileToByteArray(new File(imagePath + "\\" + employeeMaster.getEmpNumber() + ".jpeg"));
				} catch (IOException e) {
					log.info("Exception in Employee images ::{}", e.getMessage());
				}
				if (fileContent != null) {
					employeeDashboard.setEmployeeImage(
							"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent));
				}
				if (businessDetails != null) {
					employeeDashboard.setCustomer(businessDetails.getEmpbCusName());
				}
				employeeDashboardList.add(employeeDashboard);
			}
			log.info("Dashboard List Response ::{}", employeeDashboardList);
			return Utility.success(employeeDashboardList);
		}
		return Utility.fail("employee Not Found");
	}

	@Override
	public Map<String, Object> getEmployeeDetailsWithFilterEmployeeName(EmployeeDetailsDto data) {

		log.info("Get Dashboard Data:");
		List<EmployeeDashboardDto> employeeDashboardList = new ArrayList<>();
		List<EmployeeMaster> employeeDetails = new ArrayList<>();
		if (data.getEmpNumber() != null && data.getEmpName() != null) {
			employeeDetails = employeeMasterRepo.findBySearchEmployeeNumberAndName(data.getEmpNumber(),
					"%" + data.getEmpName() + "%");
		} else if (data.getEmpName() != null) {
			employeeDetails = employeeMasterRepo.findBySearchEmployeeName("%" + data.getEmpName() + "%");
		} else if (data.getEmpNumber() != null) {
			employeeDetails = employeeMasterRepo.findBySearchEmployeeNumber(data.getEmpNumber());
		}

		for (EmployeeMaster employeeMaster : employeeDetails) {
			EmployeeDashboardDto employeeDashboard = new EmployeeDashboardDto();
			EmployeeBusinessDetails businessDetails = employeeBusinessRepo.findByEmpCode(employeeMaster.getEmpCode());
			EmployeeInfoView employeeInfoView = employeeInfoRepo.findByEmpCode(employeeMaster.getEmpCode());
			employeeDashboard.setEmployeeCode(employeeMaster.getEmpNumber());
			employeeDashboard.setEmployeeName(employeeMaster.getEmpName());
			employeeDashboard
					.setEmployeeDesignation(referenceMasterRepo.getRefDescription(employeeMaster.getEmpDesignation()));
			employeeDashboard.setGrade(employeeMaster.getEmpGrade());
			employeeDashboard.setCategory(referenceMasterRepo.getRefDescription(employeeMaster.getEmpCategory()));
			employeeDashboard.setHrbp(employeeMasterRepo.getHrbpName(employeeMaster.getEmpHrbpid()));
			employeeDashboard.setReportingManager(employeeMaster.getEmpRepManager());
			employeeDashboard.setProfileCompletion(
					employeeInfoView.getEnteredPer().substring(0, employeeInfoView.getEnteredPer().indexOf(".")));
			employeeDashboard.setEmployeeId(employeeMaster.getEmpCode().toString());
			byte[] fileContent = null;
			try {
				fileContent = FileUtils
						.readFileToByteArray(new File(imagePath + "\\" + employeeMaster.getEmpNumber() + ".jpeg"));
			} catch (IOException e) {
				log.info("Exception in Employee images ::{}", e.getMessage());
			}
			if (fileContent != null) {
				employeeDashboard
						.setEmployeeImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent));
			}
			if (businessDetails != null) {
				employeeDashboard.setCustomer(businessDetails.getEmpbCusName());
			}
			employeeDashboardList.add(employeeDashboard);
		}
		log.info("Dashboard List Response ::{}", employeeDashboardList);
		return Utility.success(employeeDashboardList);
	}

}
