package vn.vsd.agro.util;

import java.math.BigDecimal;

public class NumberUtils {
	
	public static final BigDecimal TWELVE = new BigDecimal(12);
	
	public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	
	public static final int PERCENTAGE_SCALE = 2;
	
	public static final int PERCENTAGE_ROUND_TYPE = BigDecimal.ROUND_HALF_UP;
	public static final int AMOUNT_ROUND_TYPE = BigDecimal.ROUND_HALF_UP;
	
	public static BigDecimal dividePercentage(BigDecimal numerator, BigDecimal denominator, int scale) {
		if (denominator == null || denominator.signum() <= 0) {
			if (numerator == null || numerator.signum() <= 0) {
				return BigDecimal.ZERO;
			}
			
			return ONE_HUNDRED;
		}
		
		if (numerator == null || numerator.signum() <= 0) {
			return BigDecimal.ZERO;
		}
		
		return numerator.multiply(ONE_HUNDRED).divide(denominator, scale, PERCENTAGE_ROUND_TYPE);
	}
	
	public static BigDecimal dividePercentage(BigDecimal numerator, BigDecimal denominator) {
		return dividePercentage(numerator, denominator, PERCENTAGE_SCALE);
	}
	
	/*public static BigDecimal percentage(BigDecimal value) {
		return percentage(value, PERCENTAGE_SCALE);
	}*/
	
	public static BigDecimal percentage(BigDecimal value, int scale) {
		if (value == null)
			return BigDecimal.ZERO;
		
		return value.divide(ONE_HUNDRED, scale, AMOUNT_ROUND_TYPE);
	}
	
	public static BigDecimal percentage(BigDecimal value, BigDecimal percent, int scale) {
		if (value == null || percent == null || percent.signum() <= 0)
			return BigDecimal.ZERO;
		
		return percentage(value.multiply(percent), scale);
	}
	
	/*public static BigDecimal percentage(BigDecimal value, BigDecimal percent) {
		return percentage(value, percent, PERCENTAGE_SCALE);
	}*/
	
	public static final BigDecimal minValue(BigDecimal value, BigDecimal minValue) {
		if (value == null || value.compareTo(minValue) < 0) {
			return minValue;
		}
		
		return value;
	}
	
	public static final BigDecimal maxValue(BigDecimal value, BigDecimal maxValue) {
		if (value == null || value.compareTo(maxValue) > 0) {
			return maxValue;
		}
		
		return value;
	}
	
	public static final BigDecimal positiveValue(BigDecimal value) {
		return minValue(value, BigDecimal.ZERO);
	}
	
	public static final BigDecimal notNullValue(BigDecimal value) {
		return (value == null ? BigDecimal.ZERO : value);
	}
	
	public static final long comparableDouble(double value) {
		return Double.doubleToLongBits(value);
	}
	
	public static final int minBaseValue(int baseValue, int...values) {
		if (values != null) {
			for (int value : values) {
				if (value >= baseValue) {
					return value;
				}
			}
		}
		
		return baseValue;
	}
	
	public static final int parseInt(Object value, int defaultValue) {
		if (value == null) {
    		return defaultValue;
    	}
    	
    	if (value instanceof Integer) {
    		return (Integer) value;
    	}
    	
    	if (value instanceof Double) {
    		return ((Double) value).intValue();
    	}
    	
    	if (value instanceof Float) {
    		return ((Float) value).intValue();
    	}
    	
    	if (value instanceof Long) {
    		return ((Long) value).intValue();
    	}
    	
    	try {
    		return new Integer(value.toString());
    	} catch (Exception ex) {}
    	
    	return defaultValue;
	}
	
	public static final BigDecimal parseNumber(Object value, BigDecimal defaultValue) {
		if (value == null) {
    		return defaultValue;
    	}
    	
    	if (value instanceof BigDecimal) {
    		return (BigDecimal) value;
    	}
    	
    	if (value instanceof Double) {
    		return new BigDecimal((Double) value);
    	}
    	
    	if (value instanceof Float) {
    		return new BigDecimal((Float) value);
    	}
    	
    	if (value instanceof Long) {
    		return new BigDecimal((Long) value);
    	}
    	
    	if (value instanceof Integer) {
    		return new BigDecimal((Integer) value);
    	}
    	
    	try {
    		return new BigDecimal(value.toString());
    	} catch (Exception ex) {}
    	
    	return defaultValue;
	}
}
