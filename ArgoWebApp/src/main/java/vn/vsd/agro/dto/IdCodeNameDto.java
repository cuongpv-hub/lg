package vn.vsd.agro.dto;

public class IdCodeNameDto extends IdNameDto 
{
	/**
	 * 
	 */
    private static final long serialVersionUID = -8654806689442966453L;
    
	private String code;

    public IdCodeNameDto() {
	    super();
    }

	public IdCodeNameDto(String id, String code, String name) {
        super(id, name);
        
        this.code = code;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
