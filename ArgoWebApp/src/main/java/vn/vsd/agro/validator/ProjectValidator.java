package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.ProjectCreateDto;

@Component
public class ProjectValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == ProjectCreateDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "project.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "project.message.not.empty.description");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "approveName", "project.message.not.empty.approve.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "project.message.not.empty.address");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName", "project.message.not.empty.contact.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactAddress", "project.message.not.empty.contact.address");
	}
}
