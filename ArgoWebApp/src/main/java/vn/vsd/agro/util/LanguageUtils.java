package vn.vsd.agro.util;

import java.util.Locale;

public final class LanguageUtils {
	public static Locale getLocale(String lang) {
		Locale locale = Locale.getDefault();
		
		if ("en".equalsIgnoreCase(lang)) {
			locale = Locale.US;
		} else if ("vi".equalsIgnoreCase(lang)) {
			locale = new Locale("vi", "VN");
		}
		
		return locale;
	}
}
