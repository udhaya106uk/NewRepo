package com.onward.hrservice.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "emp_details_view")
public class EmployeeDetailsView {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="emp_code")
	private Integer empCode;
	@Column(name="emp_number")
	private String empNumber;
	@Column(name="emp_name")
	private String empName;
	@Column(name="emp_gender")
	private String empGender;
	@Column(name="emp_dob")
	private LocalDateTime empDob;
	@Column(name="emp_blood_group")
	private String empBloodGroup;
	@Column(name="emp_marital_status")
	private String empMaritalStatus;
	@Column(name="emp_father_or_husband_name")
	private String empFatherOrHusbandName;
	@Column(name="emp_personal_email_id")
	private String empPersonalEmailId;
	@Column(name="emp_prim_contact_no")
	private String empPrimContactNo;
	@Column(name="emp_sec_contact_no")
	private String empSecContactNo;
	@Column(name="emp_doj")
	private LocalDateTime empDoj;
	@Column(name="emp_initial_doj")
	private LocalDateTime empInitialDoj;
	@Column(name="emp_conformation_dt")
	private LocalDateTime empConformationDt;
	@Column(name="emp_conformation_status")
	private String empConformationStatus;
	@Column(name="emp_notice_period")
	private String empNoticePeriod;
	@Column(name="emp_bond_applicable")
	private String empBondApplicable;
	@Column(name="emp_amount_of_bond")
	private Integer empAmountOfBond;
	@Column(name="emp_bond_start_date")
	private LocalDateTime empBondStartDate;
	@Column(name="emp_bond_end_date")
	private LocalDateTime empBondEndDate;
	@Column(name="emp_res_date")
	private LocalDateTime empResDate;
	@Column(name="emp_ten_lwd")
	private LocalDateTime empTenLwd;
	@Column(name="emp_lwd")
	private LocalDateTime empLwd;
	@Column(name="emp_lwd_month")
	private String empLwdMonth;
	@Column(name="emp_reason")
	private String empReason;
	@Column(name="emp_grat_formula")
	private String empGratFormula;
	@Column(name="emp_fnf")
	private String empFnf;
	@Column(name="emp_previous_exp")
	private String empPreviousExp;
	@Column(name="emp_relevant_exp_bj")
	private String empRelevantExpBj;
	@Column(name="emp_initial_otl_exp")
	private String empInitialOtlExp;
	@Column(name="emp_skill_set")
	private String empSkillSet;
	@Column(name="emp_pan_number")
	private String empPanNumber;
	@Column(name="emp_aadhar_number")
	private String empAadharNumber;
	@Column(name="emp_epf_number")
	private String empEpfNumber;
	@Column(name="emp_uan_number")
	private String empUanNumber;
	@Column(name="emp_esic_number")
	private String empEsicNumber;
	@Column(name="emp_rmid")
	private Integer empRmid;
	@Column(name="emp_hrbpid")
	private String empHrbpid;
	@Column(name="emp_category")
	private String empCategory;
	@Column(name="emp_grade")
	private String empGrade;
	@Column(name="emp_designation")
	private String empDesignation;
	@Column(name="emp_status")
	private String empStatus;
	@Column(name="emp_rep_manager")
	private String empRepManager;
	@Column(name="emp_qualification")
	private String empQualification;
	@Column(name="emp_special")
	private String empSpecial;
	@Column(name="emp_certification")
	private String empCertification;
	@Column(name="empa_cur_address1")
	private String empaCurAddress1;
	@Column(name="empa_cur_address2")
	private String empaCurAddress2;
	@Column(name="empa_cur_address3")
	private String empaCurAddress3;
	@Column(name="empa_cur_pincode")
	private String empaCurPincode;
	@Column(name="empa_per_address1")
	private String empaPerAddress1;
	@Column(name="empa_per_address2")
	private String empaPerAddress2;
	@Column(name="empa_per_address3")
	private String empaPerAddress3;
	@Column(name="empa_per_pincode")
	private String empaPerPincode;
	@Column(name="empb_cost_allocation")
	private String empbCostAllocation;
	@Column(name="empb_cost_entity")
	private String empbCostEntity;
	@Column(name="empb_cost_bus_function")
	private String empbCostBusFunction;
	@Column(name="empb_dep_id")
	private String empbDepId;
	@Column(name="empb_dep_baselocation")
	private String empbDepBaselocation;
	@Column(name="empb_dep_industry")
	private String empbDepIndustry;
	@Column(name="empb_dep_subindustry")
	private String empbDepSubindustry;
	@Column(name="empb_dep_engagement")
	private String empbDepEngagement;
	@Column(name="empb_lob")
	private String empbLob;
	@Column(name="empb_lob_sub")
	private String empbLobSub;
	@Column(name="empb_lob_revenue_cat")
	private String empbLobRevenueCat;
	@Column(name="emb_tf_depu_date")
	private LocalDateTime embTfDepuDate;
	@Column(name="emb_tf_isintratransfer")
	private String embTfIsintratransfer;
	@Column(name="empb_tf_worklocation")
	private String empbTfWorklocation;
	@Column(name="empb_tf_cost")
	private String empbTfCost;
	@Column(name="empb_tf_state")
	private String empbTfState;
	@Column(name="empb_regional_bu")
	private String empbRegionalBu;
	@Column(name="empb_bu")
	private String empbBu;
	@Column(name="empb_cus_name")
	private String empbCusName;
	@Column(name="empb_cust_code")
	private String empbCustCode;
	@Column(name="empb_pd_code")
	private String empbPdCode;
	@Column(name="empb_cust_country")
	private String empbCustCountry;
	@Column(name="empb_cust_state")
	private String empbCustState;
	@Column(name="empp_country")
	private String emppCountry;
	@Column(name="empp_issue_date")
	private LocalDateTime emppIssueDate;
	@Column(name="empp_expiry_date")
	private LocalDateTime emppExpiryDate;
	@Column(name="empp_number")
	private String emppNumber;
	@Column(name="empv_visa_type")
	private String empvVisaType;
	@Column(name="empv_start_date")
	private LocalDateTime empvStartDate;
	@Column(name="empv_end_date")
	private LocalDateTime empvEndDate;
	@Column(name="age")
	private String age;
	@Column(name="onward_experience")
	private String onwardExperience;
	@Column(name="total_experience")
	private String totalExperience;
	@Column(name="Department_Name")
	private String departmentName;
	@Column(name="emp_email_address")
	private String empEmailAddress;
	@Column(name="emgc_name")
	private String emgcName;
	@Column(name="emgc_relationship")
	private String emgcRelationship;
	@Column(name="emgc_contact_number")
	private String emgcContactNumber;
	@Column(name="embk_mnh_id")
	private String embkMnhId;
	@Column(name="embk_name_asper_bank")
	private String embkNameAsperBank;
	@Column(name="embk_bank_name")
	private String embkBankName;
	@Column(name="embk_bank_acc_no")
	private String embkBankAccNo;
	@Column(name="embk_ifsc_code")
	private String embkIfscCode;
	@Column(name="emb_tf_depu_end_date")
	private LocalDateTime embTfDepuEndDate;
}
