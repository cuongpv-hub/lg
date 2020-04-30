package vn.vsd.agro.dto;

public class LoginAndLinkDto extends LoginDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8474444310875400466L;
	
	private int linkType;

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}
	
}
