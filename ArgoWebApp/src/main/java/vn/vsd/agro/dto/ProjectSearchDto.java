package vn.vsd.agro.dto;

public class ProjectSearchDto extends SearchDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7841253307465218951L;
	
	//private String name;
	private String company;
	//private boolean onlyAvaiable;
	
	// Yes/No/Empty value (Y/N)
	private String requireProduct;
	private String requireLand;
	private String requireScientist;
	
	public ProjectSearchDto () {
		super();
	}
	
	public ProjectSearchDto(SearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
		
	}
	

	public ProjectSearchDto(ProjectSearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
		this.company = dto.getCompany();
		this.requireLand = dto.getRequireLand();
		this.requireProduct = dto.getRequireProduct();
		this.requireScientist = dto.getRequireScientist();
	}
	
	/*public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}*/

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	/*public boolean isOnlyAvaiable() {
		return onlyAvaiable;
	}

	public void setOnlyAvaiable(boolean onlyAvaiable) {
		this.onlyAvaiable = onlyAvaiable;
	}*/

	public String getRequireProduct() {
		return requireProduct;
	}

	public void setRequireProduct(String requireProduct) {
		this.requireProduct = requireProduct;
	}

	public String getRequireLand() {
		return requireLand;
	}

	public void setRequireLand(String requireLand) {
		this.requireLand = requireLand;
	}

	public String getRequireScientist() {
		return requireScientist;
	}

	public void setRequireScientist(String requireScientist) {
		this.requireScientist = requireScientist;
	}
}
