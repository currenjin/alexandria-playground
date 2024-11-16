package com.tdd.util.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
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
	void validate_for_valid_excel() {
		ValidationResult actual = ExcelValidator.validate(EMPTY_EXCEL);
	}
}