package vn.vsd.agro.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.ProfileCreateDto;
import vn.vsd.agro.util.StringUtils;

@Component
public class ProfileValidator implements Validator {
	private EmailValidator emailValidator = EmailValidator.getInstance();

	// Các class được hỗ trợ Validate
	public boolean supports(Class<?> clazz) {
		return ProfileCreateDto.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		ProfileCreateDto dto = (ProfileCreateDto) target;
		
		// Kiểm tra các trường:
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "profile.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "profile.message.not.empty.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "profile.message.not.empty.address");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roles", "profile.message.not.empty.role");
		
		String phone = dto.getMobilePhone();
		if (StringUtils.isNullOrEmpty(phone)) {
			phone = dto.getCompanyPhone();
			
			if (StringUtils.isNullOrEmpty(phone)) {
				phone = dto.getHomePhone();
				
				if (StringUtils.isNullOrEmpty(phone)) {
					errors.rejectValue("mobilePhone", "profile.message.not.empty.phone");
				}
			}
		}
		
		if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
			errors.rejectValue("roles", "profile.message.invalid.role");
		}
		
		if (!emailValidator.isValid(dto.getEmail())) {
			errors.rejectValue("email", "profile.message.invalid.email.pattern");
		}
	}
}
