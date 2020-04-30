package vn.vsd.agro.dto;

import java.io.Serializable;

public class CountryCreateDto implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = -9015380834780465761L;
    
    private String id;
	private String code;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
