package vn.vsd.agro.dto;

public abstract class BaseResourceCreateDto extends BaseItemCreateDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8906605932780491021L;
	
	private String contactAddress;
	private String contactLocationId;
	private String contactDistrictId;
	private String contactProvinceId;
	private String contactCountryId;
	
	private String contactName;
	private String contactAlias;
	
	private String contactPhone;
	private String contactMobile;
	private String contactHomePhone;
	private String contactFax;
	private String contactEmail;
	
	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getContactLocationId() {
		return contactLocationId;
	}

	public void setContactLocationId(String contactLocationId) {
		this.contactLocationId = contactLocationId;
	}

	public String getContactDistrictId() {
		return contactDistrictId;
	}

	public void setContactDistrictId(String contactDistrictId) {
		this.contactDistrictId = contactDistrictId;
	}

	public String getContactProvinceId() {
		return contactProvinceId;
	}

	public void setContactProvinceId(String contactProvinceId) {
		this.contactProvinceId = contactProvinceId;
	}

	public String getContactCountryId() {
		return contactCountryId;
	}

	public void setContactCountryId(String contactCountryId) {
		this.contactCountryId = contactCountryId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAlias() {
		return contactAlias;
	}

	public void setContactAlias(String contactAlias) {
		this.contactAlias = contactAlias;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactHomePhone() {
		return contactHomePhone;
	}

	public void setContactHomePhone(String contactHomePhone) {
		this.contactHomePhone = contactHomePhone;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
}
