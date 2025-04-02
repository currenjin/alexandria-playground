package com.tdd.util.excel;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class ExcelReaderTest {
	public static final String HEADER_NAME = "기사명";
	public static final String FIRST_NAME = "홍길동";
	public static final String SECOND_NAME = "김철수";
	public static final String HEADER_PHONE_NUMBER = "전화번호";
	public static final String FIRST_PHONE_NUMBER = "010-1234-5678";
	public static final String SECOND_PHONE_NUMBER = "010-8765-4321";
	private MultipartFile VALID_EXCEL;
	private MultipartFile EMPTY_EXCEL;

	@BeforeEach
	void setUp() throws IOException {
		List<String[]> testData = Arrays.asList(
			new String[]{FIRST_NAME, FIRST_PHONE_NUMBER},
			new String[]{SECOND_NAME, SECOND_PHONE_NUMBER}
		);

		VALID_EXCEL = ExcelTestHelper.createExcelFile(testData);
		EMPTY_EXCEL = ExcelTestHelper.createEmptyExcelFile();
	}

	@Test
	void readExcel() throws IOException {
		Map<Integer, Map<String, String>> actual = ExcelReader.read(VALID_EXCEL);

		Map<String, String> firstActual = actual.get(1);
		assertEquals(FIRST_NAME, firstActual.get(HEADER_NAME));
		assertEquals(FIRST_PHONE_NUMBER, firstActual.get(HEADER_PHONE_NUMBER));

		Map<String, String> secondActual = actual.get(2);
		assertEquals(SECOND_NAME, secondActual.get(HEADER_NAME));
		assertEquals(SECOND_PHONE_NUMBER, secondActual.get(HEADER_PHONE_NUMBER));
	}

	@Test
	void readEmptyExcel() throws IOException {
		Map<Integer, Map<String, String>> actual = ExcelReader.read(EMPTY_EXCEL);

		assertTrue(actual.isEmpty());
	}
}