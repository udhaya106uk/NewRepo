package com.onward.hrservice.service;

import java.io.IOException;
import java.util.List;

public interface HRExportService {
	
	public byte[] excelData() throws IOException;
	
	public byte[] excelDataWithFilters(List<Integer> empCode) throws IOException;

}
