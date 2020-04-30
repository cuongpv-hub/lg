package vn.vsd.agro.dto;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The envelope for any response. This envelope may contains:
 * <ul>
 * 	<li>meta</li>
 * 	<li>data</li>
 * </ul>
 * 
 * @author mely
 */
public class Envelope extends HashMap<String, Object> {

	private static final long serialVersionUID = 3844487234430202272L;
    
    public Envelope() {
        super();
    }
    
    public Envelope(Meta meta) {
        this(meta, null);
    }
    
    public Envelope(Object data) {
        this(Meta.OK, data);
    }
    
    public Envelope(Meta meta, Object data) {
        super();
        this.setMeta(meta);
        this.setData(data);
    }

    public Meta getMeta() {
        return (Meta) this.get("meta");
    }

    public void setMeta(Meta meta) {
        if (meta == null) {
            if (containsKey("meta")) {
                remove("meta");
            }
            return;
        }
        this.put("meta", meta);
    }

    public Object getData() {
        return this.get("data");
    }

    public void setData(Object data) {
        if (data == null) {
            if (containsKey("data")) {
                remove("data");
            }
            return;
        }
        this.put("data", data);
    }
    
    public ResponseEntity<?> toResponseEntity() {
    	return toResponseEntity(HttpStatus.OK);
    }
    
    public ResponseEntity<?> toResponseEntity(HttpStatus status) {
    	return toResponseEntity(null, status);
    }
    
    public ResponseEntity<?> toResponseEntity(HttpServletResponse response, HttpStatus status) {
    	Object data = getData();
    	if (data == null) {
    		data = getMeta();
    	}
    	
    	HttpHeaders headers = new HttpHeaders();
    	return new ResponseEntity<Object>(data, headers, status);
    }
}
