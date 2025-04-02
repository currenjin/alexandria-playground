package com.tdd.util.excel;

import org.springframework.web.multipart.MultipartFile;

import com.tdd.util.excel.result.ValidationResult;

public class ExcelValidator {

	public static ValidationResult validate(MultipartFile file) {
		return new ValidationResult();
	}
}
