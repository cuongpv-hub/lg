package vn.vsd.agro.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleDate implements Serializable, Comparable<SimpleDate> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7631091263916269546L;

    public static final long YEAR_MULTIPLIER = 10000000000l;
    public static final long MONTH_MULTIPLIER = 100000000l;
    public static final long DATE_MULTIPLIER = 1000000l;
    
    public static final SimpleDate createByIsoDate(String isoDate, SimpleDate defaultValue) {
    	String isoFormat = "yyyy-MM-dd";
    	return parseDate(isoDate, isoFormat, defaultValue);
    }
    
    public static final SimpleDate parseDate(String dateValue, String format, SimpleDate defaultValue) {
        if (dateValue != null) {
            try {
            	SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date date = sdf.parse(dateValue);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                SimpleDate value = new SimpleDate();
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
    
    /**
     * example: 2015
     */
    private int year;

    /**
     * 0 -> 11
     */
    private int month;

    /**
     * 1 -> 31
     */
    private int date;

    @JsonIgnore
    @XmlTransient
    private long value;

    public SimpleDate() {
        this(0, 0, 1);
    }

    public SimpleDate(SimpleDate date) {
        this(date.getYear(), date.getMonth(), date.getDate());
    }

    public SimpleDate(long value) {
    	this.year = (int)(value / YEAR_MULTIPLIER);
    	
    	value = value - this.year * YEAR_MULTIPLIER;
    	this.month = (int)(value / MONTH_MULTIPLIER);
    	
    	value = value - this.month * MONTH_MULTIPLIER;
    	this.date = (int)(value / DATE_MULTIPLIER);
    	
    	this.updateValue();
    }
    
    public SimpleDate(int year, int month, int date) {
        if (!isValidData(year, month, date)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.year = year;
        this.month = month;
        this.date = date;

        updateValue();
    }

    public SimpleDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date not valid");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            this.year = cal.get(Calendar.YEAR);
            this.month = cal.get(Calendar.MONTH);
            this.date = cal.get(Calendar.DAY_OF_MONTH);
        }

        updateValue();
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        if (!isValidData(0, 0, date)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.date = date;

        updateValue();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (!isValidData(0, month, 1)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.month = month;

        updateValue();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (!isValidData(year, 0, 1)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.year = year;

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
    public int getDayOfWeek() {
        return getCalendar().get(Calendar.DAY_OF_WEEK);
    }

    @JsonIgnore
    @XmlTransient
    public String getIsoMonth() {
        return String.format("%04d-%02d", year, month + 1);
    }
    
    @JsonIgnore
    @XmlTransient
    public String getIsoDate() {
        return String.format("%04d-%02d-%02d", year, month + 1, date);
    }

    @JsonIgnore
    @XmlTransient
    public Date getDateObject() {
        return getCalendar().getTime();
    }

    /**
     * Compares two Dates for ordering.
     *
     * @param anotherDate
     *            the <code>Date</code> to be compared.
     * @return the value <code>0</code> if the argument Date is equal to this
     *         Date; a value less than <code>0</code> if this Date is before the
     *         Date argument; and a value greater than <code>0</code> if this
     *         Date is after the Date argument.
     * @since 1.2
     * @exception NullPointerException
     *                if <code>anotherDate</code> is null.
     */
    @Override
    public int compareTo(SimpleDate anotherDate) {
        long thisValue = getValue();
        long anotherValue = anotherDate.getValue();
        return (thisValue < anotherValue ? -1 : (thisValue == anotherValue ? 0 : 1));
    }

    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    
	    if (obj == null)
		    return false;
	    
	    if (getClass() != obj.getClass())
		    return false;
	    
	    SimpleDate other = (SimpleDate) obj;
	    if (value != other.value)
		    return false;
	    
	    return true;
    }

	public String format(String pattern) {
        return new SimpleDateFormat(pattern).format(getDateObject());
    }

    @Override
    public String toString() {
        return getIsoDate();
    }
    
    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (int) (value ^ (value >>> 32));
	    return result;
    }

    @JsonIgnore
    @XmlTransient
    protected Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, date);

        return c;
    }

    protected void updateValue() {
    	long value = (this.year * YEAR_MULTIPLIER) 
        		+ (this.month * MONTH_MULTIPLIER) 
        		+ (this.date * DATE_MULTIPLIER);
    	
        this.setValue(value);
    }

    protected void silentSetValue(int year, int month, int date) {
    	if (!isValidData(year, month, date)) {
            throw new IllegalArgumentException("data not valid");
        }

        this.year = year;
        this.month = month;
        this.date = date;
    }
    
    private boolean isValidData(int year, int month, int date) {
        if (year < 0) {
            return false;
        }

        if (month < 0 || month > 11) {
            return false;
        }

        if (date < 1 || date > 31) {
            return false;
        }

        return true;
    }
}
