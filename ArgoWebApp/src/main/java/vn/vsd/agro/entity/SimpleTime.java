package vn.vsd.agro.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleTime implements Serializable, Comparable<SimpleTime> {
	/**
	 * 
	 */
    private static final long serialVersionUID = -8122536302441069308L;

    public static final long HOUR_MULTIPLIER = 10000l;
    public static final long MINUTE_MULTIPLIER = 100l;
    //public static final long SECOND_MULTIPLIER = 1l;
    
    public static final SimpleTime createByIsoTime(String isoTime, SimpleTime defaultValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (isoTime != null) {
            try {
                Date date = sdf.parse(isoTime);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                SimpleTime value = new SimpleTime();
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
    private long value;

    public SimpleTime() {
        this(0, 0, 0);
    }

    public SimpleTime(SimpleTime date) {
        this(date.getHour(), date.getMinute(), date.getSecond());
    }

    public SimpleTime(long value) {
    	this.hour = (int)(value / HOUR_MULTIPLIER);
    	
    	value = value - this.hour * HOUR_MULTIPLIER;
    	this.minute = (int)(value / MINUTE_MULTIPLIER);
    	
    	this.second = (int)(value - this.minute * MINUTE_MULTIPLIER);
    	this.updateValue();
    }
    
    public SimpleTime(int hour, int minute, int second) {
        if (!isValidData(hour, minute, second)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.hour = hour;
        this.minute = minute;
        this.second = second;

        updateValue();
    }

    public SimpleTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date not valid");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

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
        if (!isValidData(hour, 0, 0)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.hour = hour;

        updateValue();
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (!isValidData(0, minute, 0)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.minute = minute;

        updateValue();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        if (!isValidData(0, 0, second)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.second = second;

        updateValue();
    }

	public long getValue() {
		return value;
	}

	protected void setValue(long value) {
		this.value = value;
	}

	@JsonIgnore
    @XmlTransient
    public String getIsoTime() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
	
	@JsonIgnore
    @XmlTransient
    public Date getDateObject() {
        return getCalendar().getTime();
    }
	
	@Override
    public int compareTo(SimpleTime anotherTime) {
		long thisValue = getValue();
        long anotherValue = anotherTime.getValue();
        return (thisValue < anotherValue ? -1 : (thisValue == anotherValue ? 0 : 1));
    }

	public String format(String pattern) {
        return new SimpleDateFormat(pattern).format(getDateObject());
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
    protected Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(0, 0, 0, hour, minute, second);
        
        return c;
    }
    
    protected void updateValue() {
        this.value = (hour * HOUR_MULTIPLIER) + (minute * MINUTE_MULTIPLIER) + second;
    }

    protected void silentSetValue(int hour, int minute, int second) {
    	if (!isValidData(hour, minute, second)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    private boolean isValidData(int hour, int minute, int second) {
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
}
