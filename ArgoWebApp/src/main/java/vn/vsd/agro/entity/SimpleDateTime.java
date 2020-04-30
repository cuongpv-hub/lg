package vn.vsd.agro.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleDateTime extends SimpleDate {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8598700577980295511L;

    public static final long HOUR_MULTIPLIER = 10000l;
    public static final long MINUTE_MULTIPLIER = 100l;
    //public static final long SECOND_MULTIPLIER = 1l;
    
	public static final SimpleDateTime createByIsoTime(String isoTime, SimpleDateTime defaultValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (isoTime != null) {
            try {
                Date date = sdf.parse(isoTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                SimpleDateTime value = new SimpleDateTime();
                value.setYear(cal.get(Calendar.YEAR));
                value.setMonth(cal.get(Calendar.MONTH));
                value.setDate(cal.get(Calendar.DAY_OF_MONTH));
                value.setHour(cal.get(Calendar.HOUR_OF_DAY));
                value.setMinute(cal.get(Calendar.MINUTE));
                value.setSecond(cal.get(Calendar.SECOND));

                return value;
            } catch (ParseException e) {
                // DO NOTHING
            }
        }

        return defaultValue;
    }

    public static final SimpleDateTime createByIsoDate(String isoDate, SimpleDateTime defaultValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (isoDate != null) {
            try {
                Date date = sdf.parse(isoDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                SimpleDateTime value = new SimpleDateTime();
                value.setYear(cal.get(Calendar.YEAR));
                value.setMonth(cal.get(Calendar.MONTH));
                value.setDate(cal.get(Calendar.DAY_OF_MONTH));

                return value;
            } catch (ParseException e) {
                // DO NOTHING
            }
        }

        return defaultValue;
    }
    
    /*public static SimpleDate truncDate(SimpleDate date) {
        return new SimpleDate(date.getYear(), date.getMonth(), date.getDate());
    }

    public static long getDuration(SimpleDate from, SimpleDate to) {
        if (from == null || to == null) {
            return 0;
        }

        return to.getCalendar().getTime().getTime() - from.getCalendar().getTime().getTime();
    }*/
    
    /**
     * hour_of_day: 0 -> 23
     */
    private int hour;

    /**
     * 0 -> 59
     */
    private int minute;

    /**
     * 0 -> 59
     */
    private int second;
    
    @JsonIgnore
    @XmlTransient
    private long dateValue;
    
    public SimpleDateTime() {
        this(0, 0, 1, 0, 0, 0);
    }

    public SimpleDateTime(SimpleDateTime date) {
        this(date.getYear(), date.getMonth(), date.getDate(), 
        		date.getHour(), date.getMinute(), date.getSecond());
    }

    public SimpleDateTime(SimpleDate date) {
        this(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
    }
    
    public SimpleDateTime(long value) {
    	int year = (int)(value / YEAR_MULTIPLIER);
    	
    	value = value - year * YEAR_MULTIPLIER;
    	int month = (int)(value / MONTH_MULTIPLIER);
    	
    	value = value - month * MONTH_MULTIPLIER;
    	int date = (int)(value / DATE_MULTIPLIER);
    	
    	this.silentSetValue(year, month, date);
    	
    	value = value - date * DATE_MULTIPLIER;
    	this.hour = (int)(value / HOUR_MULTIPLIER);
    	
    	value = value - this.hour * HOUR_MULTIPLIER;
    	this.minute = (int)(value / MINUTE_MULTIPLIER);
    	
    	this.second = (int)(value - this.minute * MINUTE_MULTIPLIER);
    	this.updateValue();
    }
    
    public SimpleDateTime(int year, int month, int date) {
        this(year, month, date, 0, 0, 0);
    }

    public SimpleDateTime(int year, int month, int date, int hour, int minute, int second) {
        if (!isValidData(year, month, date, hour, minute, second)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.silentSetValue(year, month, date);
        
        this.hour = hour;
        this.minute = minute;
        this.second = second;

        updateValue();
    }

    public SimpleDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date not valid");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int dateValue = cal.get(Calendar.DAY_OF_MONTH);
            this.silentSetValue(year, month, dateValue);
            
            this.hour = cal.get(Calendar.HOUR_OF_DAY);
            this.minute = cal.get(Calendar.MINUTE);
            this.second = cal.get(Calendar.SECOND);
        }

        updateValue();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (!isValidData(0, 0, 1, hour, 0, 0)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.hour = hour;

        updateValue();
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (!isValidData(0, 0, 1, 0, minute, 0)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.minute = minute;

        updateValue();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        if (!isValidData(0, 0, 1, 0, 0, second)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.second = second;

        updateValue();
    }

    public long getDateValue() {
		return dateValue;
	}

	public void setDateValue(long dateValue) {
		this.dateValue = dateValue;
	}

	@JsonIgnore
    @XmlTransient
    public String getIsoTime() {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d", 
        		this.getYear(), this.getMonth() + 1, this.getDate(), hour, minute, second);
    }

    @Override
    public String toString() {
        return getIsoTime();
    }
    
    @Override
    public int hashCode() {
        return getIsoTime().hashCode();
    }

    @JsonIgnore
    @XmlTransient
    @Override
    protected Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(this.getYear(), this.getMonth(), this.getDate(), hour, minute, second);

        return c;
    }

    @Override
    protected void updateValue() {
    	this.dateValue = (this.getYear() * YEAR_MULTIPLIER) 
        		+ (this.getMonth() * MONTH_MULTIPLIER) 
        		+ (this.getDate() * DATE_MULTIPLIER);
    	
    	long value =  this.dateValue
        		+ (this.hour * HOUR_MULTIPLIER) 
        		+ (this.minute * MINUTE_MULTIPLIER)
                + this.second;
    	
        this.setValue(value);
    }

    private boolean isValidData(int year, int month, int date, int hour, int minute, int second) {
        if (year < 0) {
            return false;
        }

        if (month < 0 || month > 11) {
            return false;
        }

        if (date < 1 || date > 31) {
            return false;
        }

        if (hour < 0 || hour > 23) {
            return false;
        }

        if (minute < 0 || minute > 59) {
            return false;
        }

        if (second < 0 || second > 59) {
            return false;
        }

        return true;
    }
    
    /*public static class Period implements Serializable {
        
        private static final long serialVersionUID = -8377652259670440568L;
        
        private SimpleDate fromDate;
        private SimpleDate toDate;
        
        public Period(SimpleDate fromDate, SimpleDate toDate) {
            if (fromDate == null || toDate == null) {
                throw new IllegalArgumentException();
            }
            
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        
        public SimpleDate getFromDate() {
            return fromDate;
        }

        public void setFromDate(SimpleDate fromDate) {
            this.fromDate = fromDate;
        }

        public SimpleDate getToDate() {
            return toDate;
        }

        public void setToDate(SimpleDate toDate) {
            this.toDate = toDate;
        }
        
        public Period getCopy() {
            return new Period(new SimpleDate(fromDate), new SimpleDate(toDate));
        }
        
        @Override
        public int hashCode() {
            return toString().hashCode();
        }
        
        @Override
        public String toString() {
            return fromDate.getIsoTime() + '-' + toDate.getIsoTime();
        }
    }*/
}
