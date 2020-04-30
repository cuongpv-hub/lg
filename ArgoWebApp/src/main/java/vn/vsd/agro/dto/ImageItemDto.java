package vn.vsd.agro.dto;

public class ImageItemDto extends IdNameDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 454971933155434168L;
	
	private boolean delete;

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
