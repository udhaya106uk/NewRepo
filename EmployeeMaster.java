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
@Table(name = "emp_master")
public class EmployeeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="emp_code")
	private Integer empCode;
	@Column(name="orgb_code")
	private Integer orgbCode;
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
	@Column(name="emp_deputation_applicable")
	private String empDeputationApplicable;
	@Column(name="emp_amount_of_bond")
	private Integer empAmountOfBond;
	@Column(name="emp_bond_start_date")
	private LocalDateTime empBondStartDate;
	@Column(name="emp_bond_end_date")
	private LocalDateTime empBondEndDate;
	@Column(name="emp_act_start_date")
	private LocalDateTime empActStartDate;
	@Column(name="emp_act_end_date")
	private LocalDateTime empActEndDate;
	@Column(name="emp_res_date")
	private LocalDateTime empResDate;
	@Column(name="emp_ten_lwd")
	private LocalDateTime empTenLwd;
	@Column(name="emp_lwd")
	private LocalDateTime empLwd;
	@Column(name="emp_lwd_month")
	private String empLwdMonth;
	@Column(name="emp_state")
	private String empState;
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
	@Column(name="emp_pan_doc_number")
	private Integer empPanDocNumber;
	@Column(name="emp_aadhar_number")
	private String empAadharNumber;
	@Column(name="emp_aadhar_doc_number")
	private Integer empAadharDocNumber;
	@Column(name="emp_epf_number")
	private String empEpfNumber;
	@Column(name="emp_uan_number")
	private String empUanNumber;
	@Column(name="emp_esic_number")
	private String empEsicNumber;
	@Column(name="emp_rmid")
	private String empRmid;
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
	@Column(name="status")
	private String status;
	@Column(name="created_by")
	private Integer createdBy;
	@Column(name="created_on")
	private LocalDateTime createdOn;
	@Column(name="modified_by")
	private Integer modifiedBy;
	@Column(name="modified_on")
	private LocalDateTime modifiedOn;
	@Column(name="approved_by")
	private Integer approvedBy;
	@Column(name="approved_on")
	private LocalDateTime approvedOn;
	@Column(name="emp_email_address")
	private String empEmailAddress;
	
}
