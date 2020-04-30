package vn.vsd.agro.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {
    
    public static final Meta OK = new Meta(200);
    public static final Meta ERROR = new Meta(403);
    
    @JsonProperty("code")
    private int code;

    public Meta() {
        super();
    }

    public Meta(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    public Map<String, Object> toMap() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("code", this.code);
		
		return values;
	}
}
