package vn.vsd.agro.dto;

public class ProvinceDto extends DTO<String> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -4045483894969136641L;
    
	private String code;
    private String name;
    private CountryDto country;
    
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

	public CountryDto getCountry() {
		return country;
	}

	public void setCountry(CountryDto country) {
		this.country = country;
	}
}
