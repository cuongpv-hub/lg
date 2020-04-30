package vn.vsd.agro.dto;

public class LocationDto extends DTO<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6222942001440680697L;
	
	private String code;
    private String name;
    private DistrictDto district;
    
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

	public DistrictDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictDto district) {
		this.district = district;
	}

}
