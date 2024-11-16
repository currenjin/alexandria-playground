package com.tdd.util.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelReader {
	public static Map<Integer, Map<String, String>> read(MultipartFile file) throws IOException {
		Map<Integer, Map<String, String>> result = new HashMap<>();

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			Row headerRow = sheet.getRow(0);

			String[] headers = new String[headerRow.getLastCellNum()];
			for (int i = 0; i < headerRow.getLastCellNum(); i++) {
				Cell cell = headerRow.getCell(i);
				headers[i] = getCellValue(cell);
			}

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				Map<String, String> rowData = new HashMap<>();

				for (int j = 0; j < headers.length; j++) {
					Cell cell = row.getCell(j);
					rowData.put(headers[j], getCellValue(cell));
				}

				result.put(i, rowData);
			}
		}

		return result;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) return "";

		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getLocalDateTimeCellValue().toString();
				}
				double numericValue = cell.getNumericCellValue();
				if (numericValue == (long) numericValue) {
					return String.format("%d", (long) numericValue);
				}
				return String.valueOf(numericValue);
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				try {
					return String.valueOf(cell.getNumericCellValue());
				} catch (IllegalStateException e) {
					return cell.getStringCellValue();
				}
			default:
				return "";
		}
	}
}
