package com.onward.hrservice.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onward.hrservice.common.Utility;
import com.onward.hrservice.entity.EmployeeDetailsView;
import com.onward.hrservice.repository.EmployeeDetailsViewRepository;
import com.onward.hrservice.repository.EmployeeMasterRepository;
import com.onward.hrservice.service.HRExportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HRExportServiceImpl implements HRExportService {
	
	private static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
	private static final int UNIT_OFFSET_LENGTH = 7;
	private static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109, 146, 182, 219 };
	
	@Autowired
	EmployeeMasterRepository employeeMasterRepo;

	@Autowired
	EmployeeDetailsViewRepository employeeDetailsViewRepo;
	
	@Override
	public byte[] excelData() throws IOException {
		log.info("Create Workbook");
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("All_Employees");
			log.info("Create Header column Name");
			createHeader(workbook, sheet);
			log.info("Write Records into each row");
			createRecords(workbook, sheet);
			log.info("Set auto size column width for each column");
			for (int i = 0; i < Utility.getHeadercell().length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			baos.close();
			return baos.toByteArray();
		}
	}
	
	
	@Override
	public byte[]excelDataWithFilters(List<Integer> empCode) throws IOException{
		log.info("Create Workbook");
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Employees");
			log.info("Create Header column Name");
			createHeader(workbook, sheet);
			log.info("Write Records into each row");
			createFilterRecords(workbook, sheet,empCode);
			log.info("Set auto size column width for each column");
			for (int i = 0; i < Utility.getHeadercell().length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			baos.close();
			return baos.toByteArray();
		}
	}
	
	static short pixel2WidthUnits(int pxs) {
		short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH));
		widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];
		return widthUnits;
	}
	
	public void createHeader(Workbook workbook, Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < Utility.getHeadercell().length; i++) {
			Cell createTopHeaderCell = headerRow.createCell(i);

			createTopHeaderCell.setCellValue(Utility.getHeadercell()[i]);
			createTopHeaderCell.setCellStyle(getHeaderCellStyle(workbook));
		}
	}
	
	public void createFilterRecords(Workbook workbook, Sheet sheet, List<Integer> empCode) {
		List<EmployeeDetailsView> employeeDetails = employeeDetailsViewRepo.findByEmpCode(empCode);
		insertCellValue(workbook, sheet,  employeeDetails);
	}
	
	
	public void createRecords(Workbook workbook, Sheet sheet) {
		List<EmployeeDetailsView> employeeDetails = employeeDetailsViewRepo.findAll();
		insertCellValue(workbook, sheet,  employeeDetails);
	}
	
	
	private void insertCellValue(Workbook workbook, Sheet sheet, List<EmployeeDetailsView> employeeDetails) {
		int row = 1;
		CellStyle cellBodyStyle = getCellBodyStyle(workbook);
		CellStyle cellDateBodyStyle = getCellDateBodyStyle(workbook);
		for(EmployeeDetailsView employeeDetail : employeeDetails) {
			Row headerRow = sheet.createRow(row++);
			
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpHrbpid()),0);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpNumber()),1);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpName()),2);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpStatus()),3);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpGrade()),4);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpDesignation()),5);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpCategory()),6);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpRmid()== null?null:employeeDetail.getEmpRmid().toString()),7);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpRepManager()),8);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpSkillSet()),9);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCostAllocation()),10);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCostEntity()),11);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbBu()),12);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbRegionalBu()),13);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCostBusFunction()),14);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbDepId()),15);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getDepartmentName()),16);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbDepIndustry()),17);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbDepSubindustry()),18);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbDepBaselocation()),19);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbDepEngagement()),20);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbLobRevenueCat()),21);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbLob()),22);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbLobSub()),23);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCusName()),24);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCustCode()),25);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbPdCode()),26);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCustCountry()),27);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCustState()),28);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpDoj(),29);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpInitialDoj(),30);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpGender()),31);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpDob(),32);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getAge()),33);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpPreviousExp()),34);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpRelevantExpBj()),35);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getOnwardExperience()),36);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpInitialOtlExp()),37);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getTotalExperience()),38);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpQualification()),39);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpSpecial()),40);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpCertification()),41);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpConformationDt(),42);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpConformationStatus()),43);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpNoticePeriod()),44);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpPanNumber()),45);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpMaritalStatus()),46);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpBloodGroup()),47);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpPersonalEmailId()),48);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpEmailAddress()),49);
			createCellValue(headerRow, cellBodyStyle,
					Utility.combineAddress(employeeDetail.getEmpaCurAddress1(), employeeDetail.getEmpaCurAddress2(),
							employeeDetail.getEmpaCurAddress3(), employeeDetail.getEmpaCurPincode()), 50);
			createCellValue(headerRow, cellBodyStyle,
					Utility.combineAddress(employeeDetail.getEmpaPerAddress1(), employeeDetail.getEmpaPerAddress2(),
							employeeDetail.getEmpaPerAddress3(), employeeDetail.getEmpaPerPincode()), 51);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpPrimContactNo()),52);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpSecContactNo()),53);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpFatherOrHusbandName()),54);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmgcName()),55);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmgcRelationship()),56);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmgcContactNumber()),57);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmppNumber()!=null?"Yes":"No"),58);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmppNumber()),59);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmppIssueDate(),60);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmppExpiryDate(),61);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmppCountry()),62);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpvVisaType()),63);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpvStartDate(),64);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpvEndDate(),65);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpBondApplicable()),66);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpAmountOfBond()== null?null:employeeDetail.getEmpAmountOfBond().toString()),67);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpBondStartDate(),68);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpBondEndDate(),69);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpEpfNumber()),70);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpUanNumber()),71);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpEsicNumber()),72);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpAadharNumber()),73);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbCostAllocation()),74);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpPanNumber()!=null?"10":""),75);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpUanNumber()!=null?"12":""),76);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpAadharNumber()!=null?"12":""),77);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmbTfDepuDate(),78);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmbTfDepuEndDate(),79);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbTfIsintratransfer()),80);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbTfWorklocation()),81);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpbTfState()),82);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpResDate(),83);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpTenLwd(),84);
			createCellValueForDateFormat(headerRow, cellDateBodyStyle, employeeDetail.getEmpLwd(),85);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpLwdMonth()),86);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpReason()),87);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpGratFormula()),88);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmpFnf()),89);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkMnhId()),90);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkNameAsperBank()),91);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkBankName()),92);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkBankAccNo()),93);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkIfscCode()),94);
			createCellValue(headerRow, cellBodyStyle, Utility.checkNullandTrim(employeeDetail.getEmbkIfscCode()!=null?"11":""),95);
			
		}
	}
	
	
	
	private CellStyle getHeaderCellStyle(Workbook workbook) {
		// Create a CellStyle with the font
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(getHeaderCellFont(workbook));
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK1.getIndex());
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setTopBorderColor(IndexedColors.BLACK1.getIndex());
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
		headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
		headerStyle.setFont(getHeaderCellFont(workbook));
		return headerStyle;
	}
	
	private Font getHeaderCellFont(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontName("Calibri");
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		return headerFont;
	}
	
	private CellStyle getCellBodyStyle(Workbook workbook) {
		// Styling border of cell.
		CellStyle genCellStyle = workbook.createCellStyle();

		genCellStyle.setFont(getCellFont(workbook));

		genCellStyle.setBorderBottom(BorderStyle.THIN);
		genCellStyle.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
		genCellStyle.setBorderRight(BorderStyle.THIN);
		genCellStyle.setRightBorderColor(IndexedColors.BLACK1.getIndex());
		genCellStyle.setBorderTop(BorderStyle.THIN);
		genCellStyle.setTopBorderColor(IndexedColors.BLACK1.getIndex());
		genCellStyle.setBorderLeft(BorderStyle.THIN);
		genCellStyle.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
		int builtinFormat = BuiltinFormats.getBuiltinFormat("text");
		genCellStyle.setDataFormat((short) builtinFormat);
		return genCellStyle;
	}
	
	private Font getCellFont(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setFontName("Calibri");
		headerFont.setFontHeightInPoints((short) 10.5);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		return headerFont;
	}
	
	private CellStyle getCellDateBodyStyle(Workbook workbook) {

		CreationHelper createHelper = workbook.getCreationHelper();
		short dateFormat = createHelper.createDataFormat().getFormat("MM/dd/yyyy");

		// Styling border of cell.
		CellStyle cellStyleDate = workbook.createCellStyle();

		cellStyleDate.setFont(getCellFont(workbook));

		cellStyleDate.setBorderBottom(BorderStyle.THIN);
		cellStyleDate.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
		cellStyleDate.setBorderRight(BorderStyle.THIN);
		cellStyleDate.setRightBorderColor(IndexedColors.BLACK1.getIndex());
		cellStyleDate.setBorderTop(BorderStyle.THIN);
		cellStyleDate.setTopBorderColor(IndexedColors.BLACK1.getIndex());
		cellStyleDate.setBorderLeft(BorderStyle.THIN);
		cellStyleDate.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
		cellStyleDate.setDataFormat(dateFormat);
		cellStyleDate.setAlignment(HorizontalAlignment.LEFT);

		return cellStyleDate;
	}
	
	private void createCellValue(Row headerRow, CellStyle cellBodyStyle, String data, int i) {
		Cell cell0 = headerRow.createCell(i);
		cell0.setCellValue(data);
		cell0.setCellStyle(cellBodyStyle);
	}
	
	private void createCellValueForDateFormat(Row headerRow, CellStyle cellBodyStyle, LocalDateTime data, int i) {
		Cell cell0 = headerRow.createCell(i);
		cell0.setCellValue(data);
		cell0.setCellStyle(cellBodyStyle);
	}

}
