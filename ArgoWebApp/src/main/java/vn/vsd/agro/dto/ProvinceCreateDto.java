package vn.vsd.agro.dto;

import java.io.Serializable;

public class ProvinceCreateDto implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = -2357477941398109868L;
    
    private String id;
	private String code;
    private String name;
    private String countryId;    
      
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

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
}
