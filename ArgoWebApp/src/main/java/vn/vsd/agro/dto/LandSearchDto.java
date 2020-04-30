package vn.vsd.agro.dto;


import java.util.List;

public class LandSearchDto extends SearchDto {
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841253307465218951L;
	
	
	private String farmer;
	
	private List<String> perposeIds;
	private List<String> nearIds;
	private List<String> farmerIds;
	
	
	public LandSearchDto() {
		super();
	}
	public LandSearchDto(SearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
	}
	
	public LandSearchDto(LandSearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
		this.farmer = dto.getFarmer();
		this.nearIds = dto.getNearIds();
		this.perposeIds = dto.getPerposeIds();
		this.farmerIds = dto.getFarmerIds();
	}
	
	
	
	
	

	public List<String> getPerposeIds() {
		return perposeIds;
	}






	public void setPerposeIds(List<String> perposeIds) {
		this.perposeIds = perposeIds;
	}






	public List<String> getNearIds() {
		return nearIds;
	}






	public void setNearIds(List<String> nearIds) {
		this.nearIds = nearIds;
	}






	public List<String> getFarmerIds() {
		return farmerIds;
	}






	public void setFarmerIds(List<String> farmerIds) {
		this.farmerIds = farmerIds;
	}






	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}



	

	

	

	
}
