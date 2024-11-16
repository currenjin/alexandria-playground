package com.tdd.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ExcelTestHelper {
	public static MultipartFile createEmptyExcelFile() throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = createSheet(workbook);

			setHeaderRow(sheet);

			byte[] bytes = toByteArray(workbook);

			return convertMockMultipartFile(bytes);
		}
	}

	public static MultipartFile createExcelFile(List<String[]> data) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = createSheet(workbook);

			setHeaderRow(sheet);

			for (int i = 0; i < data.size(); i++) {
				XSSFRow dataRow = sheet.createRow(i + 1);
				String[] rowData = data.get(i);
				for (int j = 0; j < rowData.length; j++) {
					dataRow.createCell(j).setCellValue(rowData[j]);
				}
			}

			byte[] bytes = toByteArray(workbook);

			return convertMockMultipartFile(bytes);
		}
	}

	private static MockMultipartFile convertMockMultipartFile(byte[] bytes) {
		return new MockMultipartFile(
			"file",
			"test.xlsx",
			"multipart/form-data",
			bytes
		);
	}

	private static byte[] toByteArray(XSSFWorkbook workbook) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		return bos.toByteArray();
	}

	private static XSSFSheet createSheet(XSSFWorkbook workbook) {
		return workbook.createSheet("Sheet1");
	}

	private static void setHeaderRow(XSSFSheet sheet) {
		XSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("기사명");
		headerRow.createCell(1).setCellValue("전화번호");
	}
}
