package vn.vsd.agro.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import vn.vsd.agro.dto.ArticleCreateDto;

@Component
public class ArticleValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return clazz == ArticleCreateDto.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "article.message.not.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "article.message.not.empty.description");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "approveName", "article.message.not.empty.approve.name");
		//ValidationUtils.rejectIfEmpty(errors, "categoryIds", "article.message.not.empty.category");
	}
}
