package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RegisterDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3097365605686338624L;
	
	// User
	private String name;
	private String password;
	private String confirmPassword;

	private List<String> roles;

	// Contact
	private String email;
	private String mobilePhone;
	private String companyPhone;
	private String homePhone;
	private String fax;

	private String address;
	private String locationId;
	private String districtId;
	private String provinceId;
	private String countryId;
	
	private MultipartFile imageFile;

	// Company
	/*private String taxCode;
	private String description;
	private List<String> companyFieldIds;
	private String typeId;*/

	// Farmer
	/*private int gender;
	private String birthDay;*/

	// Scientist
	/*private List<String> scientistMajorIds;
	private List<String> scientistFieldIds;
	private List<String> literacyIds; // Trinh do hoc van
	private String title; // chuc danh
	private String position; // chuc vu
	private String workplace; // Đơn vị công tác
*/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	/*public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}*/

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	/*public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCompanyFieldIds() {
		return companyFieldIds;
	}

	public void setCompanyFieldIds(List<String> companyFieldIds) {
		this.companyFieldIds = companyFieldIds;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public List<String> getScientistMajorIds() {
		return scientistMajorIds;
	}

	public void setScientistMajorIds(List<String> scientistMajorIds) {
		this.scientistMajorIds = scientistMajorIds;
	}

	public List<String> getScientistFieldIds() {
		return scientistFieldIds;
	}

	public void setScientistFieldIds(List<String> scientistFieldIds) {
		this.scientistFieldIds = scientistFieldIds;
	}

	public List<String> getLiteracyIds() {
		return literacyIds;
	}

	public void setLiteracyIds(List<String> literacyIds) {
		this.literacyIds = literacyIds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}*/
}
