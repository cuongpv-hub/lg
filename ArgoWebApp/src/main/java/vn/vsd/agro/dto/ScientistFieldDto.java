package vn.vsd.agro.dto;

public class ScientistFieldDto extends IdCodeNameDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1436340356916398456L;
	
	private IdCodeNameDto major;
	
	public IdCodeNameDto getMajor() {
		return major;
	}

	public void setMajor(IdCodeNameDto major) {
		this.major = major;
	}
}
