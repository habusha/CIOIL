package gov.taxes.infra.github.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import gov.taxes.infra.github.exception.ValidationError;
import gov.taxes.infra.github.validation.RegexValidation;

public class FileUtils {

	public static List<ValidationError> validate(MultipartFile file) {
		List<ValidationError> errors = new ArrayList<>();
		return errors;
	}

	private static int getFileNameLength(String origFileName) {
		int idx = origFileName.lastIndexOf('.');
		if (idx < 0) {
			return 0;
		}
		return idx;
	}

	public static boolean getFileNameValidationCharecters(String origFileName) {
		return origFileName.matches(RegexValidation.FILE_NAME_REGEX);
	}

	public static boolean isFileValid(MultipartFile file) {
		return true;
	}

}
