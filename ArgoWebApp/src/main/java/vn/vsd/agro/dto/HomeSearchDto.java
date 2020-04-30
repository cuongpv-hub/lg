package vn.vsd.agro.dto;



public class HomeSearchDto extends SearchDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4149009311473929765L;	
	
	private String name;
	private String categoryId;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	

}
