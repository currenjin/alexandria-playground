package com.tdd.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ExcelTestHelper {
	public static MultipartFile createEmptyExcelFile() throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Sheet1");

			XSSFRow headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("기사명");
			headerRow.createCell(1).setCellValue("전화번호");

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			byte[] bytes = bos.toByteArray();

			return new MockMultipartFile(
				"file",
				"test.xlsx",
				"multipart/form-data",
				bytes
			);
		}
	}
}
