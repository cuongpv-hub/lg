package vn.vsd.agro.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.RegisterDto;
import vn.vsd.agro.util.StringUtils;

@Component
public class RegisterValidator implements Validator {
	private EmailValidator emailValidator = EmailValidator.getInstance();

	// Các class được hỗ trợ Validate
	public boolean supports(Class<?> clazz) {
		return RegisterDto.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		RegisterDto dto = (RegisterDto) target;
		
		// Kiểm tra các trường:
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "register.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "register.message.not.empty.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "register.message.not.empty.confirm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "register.message.not.empty.address");
		
		String password = dto.getPassword();
		if (StringUtils.isNullOrEmpty(password)) {
			errors.rejectValue("password", "register.message.not.empty.password");
		} else {
			if (password.length() < 6) {
				errors.rejectValue("password", "register.message.invalid.password");
			}
			
			if (!password.equals(dto.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "register.message.invalid.confirm.password");
			}
		}
		
		if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
			errors.rejectValue("roles", "register.message.invalid.role");
		}
		
		String phone = dto.getMobilePhone();
		if (StringUtils.isNullOrEmpty(phone)) {
			phone = dto.getCompanyPhone();
			
			if (StringUtils.isNullOrEmpty(phone)) {
				phone = dto.getHomePhone();
				
				if (StringUtils.isNullOrEmpty(phone)) {
					errors.rejectValue("mobilePhone", "register.message.not.empty.phone");
				}
			}
		}
		
		if (!emailValidator.isValid(dto.getEmail())) {
			errors.rejectValue("email", "register.message.invalid.email.pattern");
		}
	}
}
