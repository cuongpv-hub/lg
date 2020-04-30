package vn.vsd.agro.dto;

public class DistrictDto extends DTO<String> {
	/**
	 * 
	 */
    private static final long serialVersionUID = 883577227218821286L;

    private String code;
    private String name;
    private ProvinceDto province;
    
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

	public ProvinceDto getProvince() {
		return province;
	}

	public void setProvince(ProvinceDto province) {
		this.province = province;
	}
}
