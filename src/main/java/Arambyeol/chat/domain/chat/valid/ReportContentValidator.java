package Arambyeol.chat.domain.chat.valid;

import Arambyeol.chat.domain.chat.enums.ReportContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReportContentValidator implements ConstraintValidator<ValidReportContent, ReportContent> {

	private String message;

	@Override
	public void initialize(ValidReportContent constraintAnnotation) {
		this.message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(ReportContent value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		for (ReportContent content : ReportContent.values()) {
			if (content == value) {
				return true;
			}
		}

		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
			.addConstraintViolation();
		return false;
	}
}

