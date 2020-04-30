package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.ProductCreateDto;



@Component
public class ProductValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == ProductCreateDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "product.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "product.message.not.empty.description");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName", "product.message.not.empty.contact.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactAddress", "product.message.not.empty.contact.address");
	}
}
