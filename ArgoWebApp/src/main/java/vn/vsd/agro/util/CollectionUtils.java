package vn.vsd.agro.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> Set<T> createSetFromArrayIgnoreEmpty(T...values) {
		Set<T> retValue = new HashSet<T>();
		
		if (values != null && values.length > 0) {
			for (T value : values) {
				if (value == null) {
					continue;
				} else if (value instanceof String && "".equals(value)) {
					continue;
				}
				
				retValue.add(value);
			}
		}
		
		return retValue;
	}
	
	public static <T> Set<T> createSetFromListIgnoreEmpty(List<T> values) {
		Set<T> retValue = new HashSet<T>();
		
		if (values != null) {
			for (T value : values) {
				if (value == null) {
					continue;
				} else if (value instanceof String && "".equals(value)) {
					continue;
				}
				
				retValue.add(value);
			}
		}
		
		return retValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> createListFromArrayIgnoreEmpty(T...values) {
		List<T> retValue = new ArrayList<T>();
		
		if (values != null && values.length > 0) {
			for (T value : values) {
				if (value == null) {
					continue;
				} else if (value instanceof String && "".equals(value)) {
					continue;
				}
				
				retValue.add(value);
			}
		}
		
		return retValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> createListFromArray(T...values) {
		List<T> retValue = new ArrayList<T>();
		
		if (values != null && values.length > 0) {
			for (T value : values) {
				retValue.add(value);
			}
		}
		
		return retValue;
	}
	
	public static Set<String> createSetBySplitStringIgnoreEmpty(String strValue, String delim) {
		Set<String> retValue = new HashSet<String>();
		if (strValue == null || "".equals(strValue)) {
			return retValue;
		}
		
		String[] values = strValue.split(delim);
		
		for (String value : values) {
			if (value == null)
				continue;
			
			value = value.trim();
			if ("".equals(value))
				continue;
			
			retValue.add(value);
		}
		
		return retValue;
	}
	
	public static List<String> createListBySplitStringIgnoreEmpty(String strValue, String delim) {
		List<String> retValue = new ArrayList<String>();
		if (strValue == null || "".equals(strValue)) {
			return retValue;
		}
		
		String[] values = strValue.split(delim);
		
		for (String value : values) {
			if (value == null)
				continue;
			
			value = value.trim();
			if ("".equals(value))
				continue;
			
			retValue.add(value);
		}
		
		return retValue;
	}
}
