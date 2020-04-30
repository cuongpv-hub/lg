package vn.vsd.agro.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import vn.vsd.agro.util.StringUtils;

public class ProfileCreateDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5238114680848888460L;

	// User
	private String name;
	private List<String> roles;

	private String avatar;
	private MultipartFile imageFile;
	
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
	
	// Company
	private boolean company;
	private String companyName;
	private String brandName;
	private String website;
	private String companyAddress;
	private String companyLocationId;
	private String companyDistrictId;
	private String companyProvinceId;
	private String companyCountryId;
	
	private String taxCode;
	private String taxLocation;
	
	private BigDecimal capital; // von dieu le
	private String capitalUomId;
	private Integer employees; // Tong so lao dong
	private String ownerName;

	private String contactName;
	private String contactPhone;
	private String contactFax;
	private String contactEmail;
	
	private String description;
	
	private String typeId;
	private List<String> companyFieldIds;
	private List<String> cooperateTypeIds; // hinh thuc hop tac mong muon
	private String cooperateMore; // hinh thuc hop tac mong muon khac
	
	// Farmer
	private boolean farmer;
	private int gender;
	private String birthDay;

	// Scientist
	private boolean scientist;
	private List<String> scientistMajorIds;
	private List<String> scientistFieldIds;
	private Map<String, String> fieldOthers;
	private Map<String, String> fieldDescriptions;
	private List<String> literacyIds; // Trinh do hoc van
	private String title; // chuc danh
	private String position; // chuc vu
	private String workplace; // Đơn vị công tác
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
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

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public String getTaxCode() {
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

	public void addCompanyFieldId(String companyFieldId) {
		if (!StringUtils.isNullOrEmpty(companyFieldId)) {
			if (this.companyFieldIds == null) {
				this.companyFieldIds = new ArrayList<>();
			}
			
			this.companyFieldIds.add(companyFieldId);
		}
	}
	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyLocationId() {
		return companyLocationId;
	}

	public void setCompanyLocationId(String companyLocationId) {
		this.companyLocationId = companyLocationId;
	}

	public String getCompanyDistrictId() {
		return companyDistrictId;
	}

	public void setCompanyDistrictId(String companyDistrictId) {
		this.companyDistrictId = companyDistrictId;
	}

	public String getCompanyProvinceId() {
		return companyProvinceId;
	}

	public void setCompanyProvinceId(String companyProvinceId) {
		this.companyProvinceId = companyProvinceId;
	}

	public String getCompanyCountryId() {
		return companyCountryId;
	}

	public void setCompanyCountryId(String companyCountryId) {
		this.companyCountryId = companyCountryId;
	}

	public String getTaxLocation() {
		return taxLocation;
	}

	public void setTaxLocation(String taxLocation) {
		this.taxLocation = taxLocation;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public String getCapitalUomId() {
		return capitalUomId;
	}

	public void setCapitalUomId(String capitalUomId) {
		this.capitalUomId = capitalUomId;
	}

	public Integer getEmployees() {
		return employees;
	}

	public void setEmployees(Integer employees) {
		this.employees = employees;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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

	public List<String> getCooperateTypeIds() {
		return cooperateTypeIds;
	}

	public void setCooperateTypeIds(List<String> cooperateTypeIds) {
		this.cooperateTypeIds = cooperateTypeIds;
	}

	public String getCooperateMore() {
		return cooperateMore;
	}

	public void setCooperateMore(String cooperateMore) {
		this.cooperateMore = cooperateMore;
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

	public void addScientistMajorId(String scientistMajorId) {
		if (!StringUtils.isNullOrEmpty(scientistMajorId)) {
			if (this.scientistMajorIds == null) {
				this.scientistMajorIds = new ArrayList<>();
			}
			
			this.scientistMajorIds.add(scientistMajorId);
		}
	}
	
	public List<String> getScientistFieldIds() {
		return scientistFieldIds;
	}

	public void setScientistFieldIds(List<String> scientistFieldIds) {
		this.scientistFieldIds = scientistFieldIds;
	}

	public void addScientistFieldId(String scientistFieldId) {
		if (!StringUtils.isNullOrEmpty(scientistFieldId)) {
			if (this.scientistFieldIds == null) {
				this.scientistFieldIds = new ArrayList<>();
			}
			
			this.scientistFieldIds.add(scientistFieldId);
		}
	}
	
	public Map<String, String> getFieldOthers() {
		return fieldOthers;
	}

	public void setFieldOthers(Map<String, String> fieldOthers) {
		this.fieldOthers = fieldOthers;
	}

	public void setFieldOther(String majorId, String field) {
		if (!StringUtils.isNullOrEmpty(majorId)) {
			if (this.fieldOthers == null) {
				this.fieldOthers = new HashMap<>();
			}
			
			this.fieldOthers.put(majorId, field);
		}
	}
	
	public boolean containsFieldOther(String majorId) {
		if (this.fieldOthers == null) {
			return false;
		}
		
		return this.fieldOthers.containsKey(majorId);
	}
	
	public Map<String, String> getFieldDescriptions() {
		return fieldDescriptions;
	}

	public void setFieldDescriptions(Map<String, String> fieldDescriptions) {
		this.fieldDescriptions = fieldDescriptions;
	}

	public void setFieldDescription(String majorId, String description) {
		if (!StringUtils.isNullOrEmpty(majorId)) {
			if (this.fieldDescriptions == null) {
				this.fieldDescriptions = new HashMap<>();
			}
			
			this.fieldDescriptions.put(majorId, description);
		}
	}
	
	public boolean containsFieldDescription(String majorId) {
		if (this.fieldDescriptions == null) {
			return false;
		}
		
		return this.fieldDescriptions.containsKey(majorId);
	}
	
	public List<String> getLiteracyIds() {
		return literacyIds;
	}

	public void setLiteracyIds(List<String> literacyIds) {
		this.literacyIds = literacyIds;
	}

	public void addLiteracyId(String literacyId) {
		if (!StringUtils.isNullOrEmpty(literacyId)) {
			if (this.literacyIds == null) {
				this.literacyIds = new ArrayList<>();
			}
			
			this.literacyIds.add(literacyId);
		}
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
	}
}
