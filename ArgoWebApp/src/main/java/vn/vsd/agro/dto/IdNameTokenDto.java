package vn.vsd.agro.dto;

public class IdNameTokenDto extends IdNameDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6221160582006746018L;
	
	private String token;

	public IdNameTokenDto() {
		super();
	}

	public IdNameTokenDto(String id, String name, String token) {
		super(id, name);
		
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
