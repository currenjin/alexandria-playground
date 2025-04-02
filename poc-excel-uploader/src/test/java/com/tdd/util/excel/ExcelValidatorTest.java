package com.tdd.util.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import com.tdd.util.excel.result.ValidationResult;

class ExcelValidatorTest {
	private MultipartFile EMPTY_EXCEL;

	@BeforeEach
	void setUp() throws IOException {
		EMPTY_EXCEL = ExcelTestHelper.createEmptyExcelFile();
	}

	@Test
	void validate_has_valid_and_invalid_results() {
		ValidationResult actual = ExcelValidator.validate(EMPTY_EXCEL);

		assertEquals(0, actual.getValidResults());
		assertEquals(0, actual.getInvalidResults());
	}

	@Test
	@Disabled
	void validate_for_valid_excel() throws IOException {
		List<String[]> testData = Arrays.asList(
			new String[]{"홍길동", "010-1234-5678"},
			new String[]{"김철수", "010-8765-4321"}
		);
		MultipartFile validTwoRowExcel = ExcelTestHelper.createExcelFile(testData);

		ValidationResult actual = ExcelValidator.validate(validTwoRowExcel);

		assertEquals(2, actual.getValidResults());
		assertEquals(0, actual.getInvalidResults());
	}
}