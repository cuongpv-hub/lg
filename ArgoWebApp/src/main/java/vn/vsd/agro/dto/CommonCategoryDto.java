package vn.vsd.agro.dto;

public class CommonCategoryDto extends DTO<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2332402895686397887L;
	
	private int type;
	private String typeName;

	private String code;
	private String name;
	private int index;
	private boolean main;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

}
