package vn.vsd.agro.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.entity.SimpleDateTime;
import vn.vsd.agro.entity.SimpleTime;

public final class DateTimeUtils {

    public static final long MILLISECONDS_BY_DAY = 24 * 60 * 60 * 1000;
    public static final int DEFAULT_MINIMAL_DAYS_IN_FIRST_WEEK = 4;
    public static final int DEFAULT_FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    
    /*********** SIMPLE DATE UTILS *******************************/
    public static final SimpleDateTime getSimpleTime(Date date) {
        return new SimpleDateTime(date);
    }
    
    /**
     * Adds a number of hour to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static SimpleDateTime addHours(SimpleDateTime date, int amount) {
        return new SimpleDateTime(DateUtils.addHours(date.getDateObject(), amount));
    }

    public static SimpleDate addHours(SimpleDate date, int amount) {
        return new SimpleDate(DateUtils.addHours(date.getDateObject(), amount));
    }
    
    /**
     * Adds a number of minutes to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static SimpleDateTime addMinutes(SimpleDateTime date, int amount) {
        return new SimpleDateTime(DateUtils.addMinutes(date.getDateObject(), amount));
    }

    public static SimpleDate addMinutes(SimpleDate date, int amount) {
        return new SimpleDate(DateUtils.addMinutes(date.getDateObject(), amount));
    }
    
    /**
     * Adds a number of days to a date returning a new object. The original date
     * object is unchanged.
     *
     * @param date
     *            the date, not null
     * @param amount
     *            the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException
     *             if the date is null
     */
    public static SimpleDateTime addDays(SimpleDateTime date, int amount) {
        return new SimpleDateTime(DateUtils.addDays(date.getDateObject(), amount));
    }

    public static SimpleDate addDays(SimpleDate date, int amount) {
        return new SimpleDate(DateUtils.addDays(date.getDateObject(), amount));
    }
    
    /**
     * Adds a number of weeks to a date returning a new object. The original
     * date object is unchanged.
     *
     * @param date
     *            the date, not null
     * @param amount
     *            the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException
     *             if the date is null
     */
    public static SimpleDateTime addWeeks(SimpleDateTime date, int amount) {
        return new SimpleDateTime(DateUtils.addWeeks(date.getDateObject(), amount));
    }

    public static SimpleDate addWeeks(SimpleDate date, int amount) {
        return new SimpleDate(DateUtils.addWeeks(date.getDateObject(), amount));
    }
    
    /**
     * Adds a number of months to a date returning a new object. The original
     * date object is unchanged.
     *
     * @param date
     *            the date, not null
     * @param amount
     *            the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException
     *             if the date is null
     */
    public static SimpleDateTime addMonths(SimpleDateTime date, int amount) {
        return new SimpleDateTime(DateUtils.addMonths(date.getDateObject(), amount));
    }

    public static SimpleDate addMonths(SimpleDate date, int amount) {
        return new SimpleDate(DateUtils.addMonths(date.getDateObject(), amount));
    }
    
    /**
     * <p>
     * Truncate this date, leaving the field specified as the most significant
     * field.
     * </p>
     *
     * <p>
     * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you
     * passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was
     * passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
     * </p>
     * 
     * @param date
     *            the date to work with
     * @param field
     *            the field from <code>Calendar</code> or
     *            <code>SEMI_MONTH</code>
     * @return the rounded date
     * @throws IllegalArgumentException
     *             if the date is <code>null</code>
     * @throws ArithmeticException
     *             if the year is over 280 million
     */
    public static SimpleDateTime truncate(SimpleDateTime date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return new SimpleDateTime(DateUtils.truncate(date.getDateObject(), field));
    }

    public static SimpleDateTime truncate(SimpleDateTime date) {
        return truncate(date, Calendar.DATE);
    }
    
    public static final SimpleDateTime getToday() {
    	Date today = DateUtils.truncate(getSystemDate(), Calendar.DATE);
    	return new SimpleDateTime(today);
    }

    public static final SimpleDateTime endOfDate(SimpleDateTime date) {
    	Date nextDay = DateUtils.truncate(DateUtils.addDays(date.getDateObject(), 1), Calendar.DATE);
    	Date endOfDay = DateUtils.addSeconds(nextDay, -1);
    	return new SimpleDateTime(endOfDay);
    }
    
    public static final SimpleDateTime endOfDate(SimpleDate date) {
    	Date nextDay = DateUtils.addDays(date.getDateObject(), 1);
    	Date endOfDay = DateUtils.addSeconds(nextDay, -1);
    	return new SimpleDateTime(endOfDay);
    }
    
    public static final SimpleDateTime endOfToday() {
    	Date tomorrow = DateUtils.truncate(DateUtils.addDays(getSystemDate(), 1), Calendar.DATE);
    	Date endToday = DateUtils.addSeconds(tomorrow, -1);
    	return new SimpleDateTime(endToday);
    }
    
    public static final SimpleDate getTomorrow() {
    	Date tomorrow = DateUtils.truncate(DateUtils.addDays(getSystemDate(), 1), Calendar.DATE);
    	return new SimpleDate(tomorrow);
    }

    public static final SimpleDate getYesterday() {
    	Date yesterday = DateUtils.truncate(DateUtils.addDays(getSystemDate(), -1), Calendar.DATE);
    	return new SimpleDate(yesterday);
    }
    
    public static final SimpleDate firstOfThisMonth() {
    	Date thisMonth = DateUtils.truncate(getSystemDate(), Calendar.MONTH);
    	return new SimpleDate(thisMonth);
    }

    public static final SimpleDate firstOfLastMonth() {
    	Date lastMonth = DateUtils.truncate(DateUtils.addMonths(getSystemDate(), -1), Calendar.MONTH);
    	return new SimpleDate(lastMonth);
    }

    public static final SimpleDateTime getCurrentTime() {
        return new SimpleDateTime(getSystemDate());
    }

    public static final boolean isSameYear(SimpleDate date1, SimpleDate date2) {
        if (date1 == null || date2 == null) {
        	return false;
        }

        return date1.getYear() == date2.getYear();
    }
    
    public static final boolean isSameDate(SimpleDate date1, SimpleDate date2) {
        if (date1 == null || date2 == null) {
        	return false;
        }

        return date1.getYear() == date2.getYear() 
                && date1.getMonth() == date2.getMonth()
                && date1.getDate() == date2.getDate();
    }
    
    public static final boolean isInBoundDate(SimpleDateTime date, SimpleDateTime date1, SimpleDateTime date2) {
        if (date == null || date1 == null || date2 == null) {
        	return false;
        }
        
        long timeValue = truncate(date, Calendar.DATE).getDateObject().getTime();
        long timeValue1 = truncate(date1, Calendar.DATE).getDateObject().getTime();
        long timeValue2 = truncate(date2, Calendar.DATE).getDateObject().getTime();
        
        return timeValue >= timeValue1 && timeValue <= timeValue2;
    }
    
    public static final boolean isInBoundDate(SimpleDateTime date, long date1, long date2) {
        if (date == null) {
        	return false;
        }
        
        long timeValue = truncate(date, Calendar.DATE).getDateObject().getTime();
        return timeValue >= date1 && timeValue <= date2;
    }
    
    public static final boolean isInBoundDate(SimpleDate date, long date1, long date2) {
        if (date == null) {
        	return false;
        }
        
        long timeValue = date.getDateObject().getTime();
        return timeValue >= date1 && timeValue <= date2;
    }
    
    public static final boolean equals(SimpleDateTime date1, SimpleDateTime date2) {
        if (date1 == null) {
        	if (date2 == null) {
        		return true;
        	}
        	
        	return false;
        } else if (date2 == null) {
        	return false;
        }

        return date1.getValue() == date2.getValue();
    }
    
    public static final boolean equals(SimpleDate date1, SimpleDate date2) {
        if (date1 == null) {
        	if (date2 == null) {
        		return true;
        	}
        	
        	return false;
        } else if (date2 == null) {
        	return false;
        }

        return date1.getValue() == date2.getValue();
    }
    
    public static final boolean equals(SimpleTime time1, SimpleTime time2) {
        if (time1 == null) {
        	if (time2 == null) {
        		return true;
        	}
        	
        	return false;
        } else if (time2 == null) {
        	return false;
        }

        return time1.getValue() == time2.getValue();
    }
    
    public static SimpleDateTime copyTime(SimpleDate date, Date source) {
    	int[] timeParts = getTimeParts(source);
    	return new SimpleDateTime(date.getYear(), date.getMonth(), date.getDate(), 
    			timeParts[0], timeParts[1], timeParts[2]);
    }
    
    /******* DATE UTILS ****************/
    public static final long getSystemTime() {
        return (new Date()).getTime();
    }
    
    public static final Date getSystemDate() {
        return new Date();
    }
    
    public static int getWeekOfYear(SimpleDateTime date, int firstDayOfWeek, int minimalDaysInFirstWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.setTime(date.getDateObject());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static int[] getDateParts(Date date) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        return new int[] {
        	cal.get(Calendar.DATE),
        	cal.get(Calendar.MONTH) + 1,
        	cal.get(Calendar.YEAR)
        };
    }
    
    public static int[] getTimeParts(Date date) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        return new int[] {
        	cal.get(Calendar.HOUR_OF_DAY),
        	cal.get(Calendar.MINUTE),
        	cal.get(Calendar.SECOND)
        };
    }
    
    public static int getDatePart(Date date, int part) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	
    	return calendar.get(part);
    }
    
    public static int getHour(Date date) {
    	return getDatePart(date, Calendar.HOUR_OF_DAY);
    }
    
    public static Date copyTime(Date date, Date source) {
    	int[] timeParts = getTimeParts(source);
    	
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, timeParts[0]);
        cal.set(Calendar.MINUTE, timeParts[1]);
        cal.set(Calendar.SECOND, timeParts[2]);
        
        return cal.getTime();
    }
    
    public static long daysBetween(Date d1, Date d2){
    	long difference = (d1.getTime() - d2.getTime() + 60000l) / 86400000;
		return Math.abs(difference);
    }
    
    public static long diffDays(Date d1, Date d2){
    	return (d1.getTime() - d2.getTime() + 60000l) / 86400000l;
    }
    
    public static long hoursBetween(Date d1, Date d2){ 
    	long difference = (d1.getTime() - d2.getTime() + 60000l) / 3600000l;
		return Math.abs(difference);
    }
    
    public static long diffHours(Date d1, Date d2){ 
    	return (d1.getTime() - d2.getTime() + 60000l) / 3600000l;
    }
    
    public static int getDayOfWeek(Date date) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    public static Date parseDate(Object value, String format, Date defaultValue) {
    	if (value == null) {
    		return defaultValue;
    	}
    	
    	if (value instanceof Date) {
    		return (Date) value;
    	}
    	
    	if (value instanceof SimpleDateTime) {
    		return ((SimpleDateTime) value).getDateObject();
    	}
    	
    	if (value instanceof BigDecimal) {
    		return new Date(((BigDecimal)value).longValue());
    	}
    	
    	if (value instanceof Double) {
    		return new Date(((Double)value).longValue());
    	}
    	
    	if (value instanceof Long) {
    		return new Date(((Long)value).longValue());
    	}
    	
    	if (value instanceof Integer) {
    		return new Date(((Integer)value).longValue());
    	}
    	
    	String stringValue = null;
    	if (value instanceof String) {
    		stringValue = (String) value;
    	} else {
    		stringValue = value.toString();
    	}
    	
    	try {
    		return new SimpleDateFormat(format).parse(stringValue);
    	} catch (Exception ex) {}
    	
    	return defaultValue;
    }
    
    public static Date truncate(Date date, int field) {
    	return DateUtils.truncate(date, field);
    }
    
    public static Date truncate(Date date) {
    	return truncate(date, Calendar.DATE);
    }
    
    /*public static String simpleDateToString(SimpleDate date) {
    	if (date == null || date.getValue() <= 0) {
    		return "";
    	}
    	
    	Date dateValue = date.getDateObject();
    	SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return dateFormat.format(dateValue);
    }
    
    public static SimpleDate stringToSimpleDate(String date) {
    	if (StringUtils.isNullOrEmpty(date)) {
    		return null;
    	}
    	
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    		Date dateValue = dateFormat.parse(date);
    		return new SimpleDate(dateValue);
		} catch (ParseException e) {}
    	
    	return null;
    }*/
}
