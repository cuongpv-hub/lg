package vn.vsd.agro.entity;

import java.util.HashMap;
import java.util.Map;

public class DbFileMeta extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -66958133819893089L;

	public DbFileMeta() {
        this(false);
    }

    public DbFileMeta(boolean used) {
        super();
        this.setUsed(used);
    }

    public DbFileMeta(Map<? extends String, ? extends Object> m) {
        super(m);
    }

    public boolean isUsed() {
    	Object value = this.get("used");
    	if (value != null && value instanceof Boolean) {
    		return ((Boolean)value).booleanValue();
    	}
    	
        return false;
    }

    public void setUsed(boolean used) {
        this.put("used", used);
    }
}
