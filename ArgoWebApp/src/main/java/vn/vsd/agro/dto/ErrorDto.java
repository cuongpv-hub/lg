package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ErrorDto implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = 657218779732781457L;

    private String code;
    private String message;
    
    public ErrorDto(String message) {
	    this(404, message);
    }

	public ErrorDto(String code, String message) {
	    super();
	    
	    this.code = code;
	    this.message = message;
    }

	public ErrorDto(int code, String message) {
	    super();
	    
	    this.code = String.valueOf(code);
	    this.message = message;
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("code", this.code);
		values.put("message", this.message);
		
		return values;
	}
}
