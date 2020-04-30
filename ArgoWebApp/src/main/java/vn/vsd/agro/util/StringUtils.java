package vn.vsd.agro.util;

import java.util.Collection;
import java.util.regex.Pattern;


public class StringUtils {
	
	/** codau. */
	private static char VIET_CHARS[] = { 'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'À',
			'Á', 'Ả', 'Ã', 'Ạ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ', 'Â', 'Ầ', 'Ấ', 'Ẩ', 'Ẫ', 'Ậ', 'è', 'é', 'ẻ', 'ẽ', 'ẹ',
			'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ', 'ì', 'í', 'ỉ', 'ĩ',
			'ị', 'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở',
			'ỡ', 'ợ', 'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ', 'ù', 'ú',
			'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự', 'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'Ỳ', 'Ý',
			'Ỷ', 'Ỹ', 'Ỵ', 'đ', 'Đ', 'Ư', 'Ừ', 'Ử', 'Ữ', 'Ứ', 'Ự' 
	};
	
	/** khongdau. */
	private static char NORMAL_CHARS[] = { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
			'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'e', 'e', 'e', 'e',
			'e', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'i', 'i', 'i',
			'i', 'i', 'I', 'I', 'I', 'I', 'I', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o',
			'o', 'o', 'o', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'u',
			'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'U', 'U', 'U', 'U', 'U', 'y', 'y', 'y', 'y', 'y', 'Y',
			'Y', 'Y', 'Y', 'Y', 'd', 'D', 'U', 'U', 'U', 'U', 'U', 'U' 
	};
	
	public static String toOracleSearchLikeSuffix(String searchText) {
		return toOracleSearchLike(searchText);
	}
	
	public static String toOracleSearchLike(String searchText) {
		String escapeChar = "/";
		String[] arrSpPat = {"/", "%", "_"};
		
		for (String str: arrSpPat) {
			if (!StringUtils.isNullOrEmpty(searchText)) {
				searchText = searchText.replaceAll(str, escapeChar + str);
			}
		}
		searchText = "%" + searchText + "%";
		return searchText;
	}
	
 	public static boolean isNullOrEmpty(final String s) {
 		return isNullOrEmpty(s, true);
 	}

 	public static boolean isNullOrEmpty(final String s, boolean trim) {
 		if (s == null || "".equals(s)) {
 			return true;
 		}

 		return (trim && "".equals(s.trim()));
 	}

 	public static String trimText(String text) {
 		if (text == null) {
 			return null;
 		}
 		
 		return text.trim();
 	}
 	
 	public static boolean equals(final String s1, final String s2, boolean trim, boolean ignoreCase) {
 		if (s1 == null) {
 			if (s2 == null) {
 				return true;
 			}
 			
 			return false;
 		} else if (s2 == null) {
 			return false;
 		}
 		
 		if (trim && ignoreCase) {
 			return s1.trim().equalsIgnoreCase(s2.trim());
 		} else if (trim) {
 			return s1.trim().equals(s2.trim());
 		} else if (ignoreCase) {
 			return s1.equalsIgnoreCase(s2);
 		}
 		
 		return s1.equals(s2);
 	}
 	
 	public static String arrayToDelimitedString(String[] arr, String delim) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		
		if (arr.length == 1) {
			return ((arr[0] == null) ? "" : arr[0]);
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null && !arr[i].isEmpty()) {
				if (sb.length() > 0) {
					sb.append(delim);
				}
			
				sb.append(arr[i]);
			}
		}
		
		return sb.toString();
	}
 	
 	public static String collectionToDelimitedString(Collection<String> arr, String delim) {
 		if (arr == null || arr.isEmpty()) {
 			return "";
 		}
 		
 		StringBuilder sb = new StringBuilder();
 		boolean withDelim = false;
		for (String value : arr) {
			if (value == null || value.isEmpty())
				continue;
			
			if (withDelim)
				sb.append(delim);
			
			sb.append(value);
			withDelim = true;
		}
		
		return sb.toString();
 	}
 	
 	public static boolean containsString(Collection<String> values, 
 			String text, boolean trim, boolean ignoreCase) {
 		if (values == null || values.isEmpty() 
 				|| text == null || text.length() == 0) {
 			return false;
 		}
 		
 		for (String value : values) {
 			if (value != null) {
	 			if (trim) {
	 				value = value.trim();
	 			}
	 			
	 			if (ignoreCase) {
	 				if (value.equalsIgnoreCase(text)) {
	 					return true;
	 				}
	 			} else if (value.equals(text)) {
	 				return true;
	 			}
 			}
 		}
 		
 		return false;
 	}
 	
 	public static boolean containsString(Collection<String> values, String text) {
 		return containsString(values, text, true, true);
 	}
 	
 	public static String getSearchableString(String input) {
		if (isNullOrEmpty(input)) {
			return "";
		}
		
		return Pattern.quote(getSearchableClearString(input));
	}
 	
 	public static String getSearchableClearString(String input) {
		if (isNullOrEmpty(input)) {
			return "";
		}
		
		input = input.toLowerCase().trim();
		for (int i = 0; i < VIET_CHARS.length; i++) {
			input = input.replace(VIET_CHARS[i], NORMAL_CHARS[i]);
		}
		
		return input;
	}

 	public static String substringWords(String text, final int length, final String separator) {

 	    if (text.length() <= length) {
 	        return text;
 	    }

 	    int pos = text.lastIndexOf(separator, length);
 	    if (pos >= 0) {
 	        text = text.substring(0, pos).trim();
 	        
 	        if (!text.endsWith(".") && !text.endsWith("?") && !text.endsWith("!")) {
 	        	text += "...";
 	        }
 	        
 	        return text;
 	    }

 	    pos = text.indexOf(separator, length);
 	    if (pos >= 0) {
 	        text = text.substring(0, pos);
 	        
 	       if (!text.endsWith(".") && !text.endsWith("?") && !text.endsWith("!")) {
	        	text += "...";
	        }
	        
	        return text;
 	    }

 	    return text;
 	}
}
