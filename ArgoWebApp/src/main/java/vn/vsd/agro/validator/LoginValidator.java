package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.LoginDto;

@Component
public class LoginValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == LoginDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account", "login.message.not.empty.account");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "login.message.not.empty.password");
	}
}
