package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.NewsCreateDto;

@Component
public class NewsValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == NewsCreateDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "news.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "news.message.not.empty.description");
		ValidationUtils.rejectIfEmpty(errors, "categoryIds", "news.message.not.empty.category");
	}
}
