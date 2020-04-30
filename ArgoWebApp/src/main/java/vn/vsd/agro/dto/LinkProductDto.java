package vn.vsd.agro.dto;

public class LinkProductDto extends ProductSimpleDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 860638201001849433L;
	
	private String linkId;
	
	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
}
