package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.POEmbed;

public class ContactEmbed extends POEmbed<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6452224512259297215L;
	
	private String name;
	private String alias;

	private String companyPhone;
	private String homePhone;
	private String mobilePhone;
	private String fax;
	private String email;
	
	private String address;
	private LocationEmbed location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationEmbed getLocation() {
		return location;
	}

	public void setLocation(LocationEmbed location) {
		this.location = location;
	}
}
