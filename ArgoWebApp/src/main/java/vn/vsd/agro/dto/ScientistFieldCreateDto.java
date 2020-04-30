package vn.vsd.agro.dto;

import java.io.Serializable;

public class ScientistFieldCreateDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2888314306240572320L;

	private String id;
	private String code;
	private String name;
	private String majorId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

}
