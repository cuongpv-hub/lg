package vn.vsd.agro.dto;

import java.util.List;

public class ProductSearchDto extends SearchDto {
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841253307465218951L;

	
	private String farmer;
	private List<String> farmerIds;
	
	public ProductSearchDto() {
		super();
	}
	
	public ProductSearchDto(SearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
	}
	
	public ProductSearchDto(ProductSearchDto dto) {
		super(dto);
		
		this.farmer = dto.getFarmer();
		this.farmerIds = dto.getFarmerIds();
		// TODO Auto-generated constructor stub
	}

	

	public String getFarmer() {
		return farmer;
	}

	public void setFarmer(String farmer) {
		this.farmer = farmer;
	}

	public List<String> getFarmerIds() {
		return farmerIds;
	}

	public void setFarmerIds(List<String> farmerIds) {
		this.farmerIds = farmerIds;
	}

	
}
