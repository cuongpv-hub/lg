package vn.vsd.agro.dto;

import java.io.Serializable;

public class LoginedUserDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 860222702159723659L;

	private String userId;
	private String name;
	private String address;
	private String email;
	private String phone;

	private boolean company;
	private boolean farmer;
	private boolean scientist;
	private boolean admin;
	private boolean deptAdmin;

	private String accessToken;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public boolean isFarmer() {
		return farmer;
	}

	public void setFarmer(boolean farmer) {
		this.farmer = farmer;
	}

	public boolean isScientist() {
		return scientist;
	}

	public void setScientist(boolean scientist) {
		this.scientist = scientist;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isDeptAdmin() {
		return deptAdmin;
	}

	public void setDeptAdmin(boolean deptAdmin) {
		this.deptAdmin = deptAdmin;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
