package vn.vsd.agro.validator;

import java.text.NumberFormat;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

public class VietnamNumberEditor extends CustomNumberEditor {

	public VietnamNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty)
			throws IllegalArgumentException {
		super(numberClass, allowEmpty);
	}

	public VietnamNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty)
			throws IllegalArgumentException {
		super(numberClass, numberFormat, allowEmpty);
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		if (value == null) {
			return "";
		}
		
		String textValue = value.toString();
		return textValue;
	}

}
