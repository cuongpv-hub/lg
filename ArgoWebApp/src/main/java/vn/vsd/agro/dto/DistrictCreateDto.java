package vn.vsd.agro.dto;

import java.io.Serializable;

public class DistrictCreateDto implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6532167108847005238L;
    
	private String code;
    private String name;
    private String provinceId;
    
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

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
}
