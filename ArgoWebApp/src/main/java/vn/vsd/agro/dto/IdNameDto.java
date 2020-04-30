package vn.vsd.agro.dto;

public class IdNameDto extends IdDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8647448205744455576L;
	
	private String name;

	public IdNameDto() {
		super();
	}

	public IdNameDto(String id) {
		super(id);
	}

	public IdNameDto(String id, String name) {
		super(id);
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
