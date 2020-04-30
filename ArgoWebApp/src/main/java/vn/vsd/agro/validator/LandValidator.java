package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.LandCreateDto;

@Component
public class LandValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == LandCreateDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "land.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "land.message.not.empty.description");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName", "land.message.not.empty.contact.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactAddress", "land.message.not.empty.contact.address");
	}
}
