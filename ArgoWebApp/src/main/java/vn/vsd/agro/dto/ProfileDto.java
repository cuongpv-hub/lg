package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.util.ConstantUtils;

public class ProfileDto extends ContactDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -503208384954259536L;

	// Company
	private boolean company;
	private String taxCode;
	private String description;
	private List<IdCodeNameDto> companyFields;
	private IdCodeNameDto type;

	// all scientists that linked with our company through projects
	private List<LinkScientistDto> linkedScientists;
	
	// Scientist & Farmer
	private boolean farmer;
	private boolean scientist;
	private int gender;
	private String birthDay;

	// Scientist
	private List<IdCodeNameDto> scientistMajors;
	private List<IdCodeNameDto> literacies;
	private String title;
	private String position;
	private String workplace;

	private boolean editable;
	
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

	public List<IdCodeNameDto> getCompanyFields() {
		return companyFields;
	}

	public void setCompanyFields(List<IdCodeNameDto> companyFields) {
		this.companyFields = companyFields;
	}

	public void addCompanyField(IdCodeNameDto companyField) {
		if (companyField != null) {
			if (this.companyFields == null) {
				this.companyFields = new ArrayList<>();
			}
			
			this.companyFields.add(companyField);
		}
	}
	
	public IdCodeNameDto getType() {
		return type;
	}

	public void setType(IdCodeNameDto type) {
		this.type = type;
	}

	public List<LinkScientistDto> getLinkedScientists() {
		return linkedScientists;
	}

	public void setLinkedScientists(List<LinkScientistDto> linkedScientists) {
		this.linkedScientists = linkedScientists;
	}

	public void addLinkedScientist(LinkScientistDto linkedScientist) {
		if (linkedScientist != null) {
			if (this.linkedScientists == null) {
				this.linkedScientists = new ArrayList<>();
			}
			
			this.linkedScientists.add(linkedScientist);
		}
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

	public boolean isMale() {
		return this.gender == ConstantUtils.GENDER_MALE;
	}
	
	public boolean isFemale() {
		return this.gender == ConstantUtils.GENDER_FEMALE;
	}
	
	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public List<IdCodeNameDto> getScientistMajors() {
		return scientistMajors;
	}

	public void setScientistMajors(List<IdCodeNameDto> scientistMajors) {
		this.scientistMajors = scientistMajors;
	}

	public void addScientistMajor(IdCodeNameDto scientistMajor) {
		if (scientistMajor != null) {
			if (this.scientistMajors == null) {
				this.scientistMajors = new ArrayList<>();
			}
			
			this.scientistMajors.add(scientistMajor);
		}
	}
	
	public List<IdCodeNameDto> getLiteracies() {
		return literacies;
	}

	public void setLiteracies(List<IdCodeNameDto> literacies) {
		this.literacies = literacies;
	}

	public void addLiteracy(IdCodeNameDto literacy) {
		if (literacy != null) {
			if (this.literacies == null) {
				this.literacies = new ArrayList<>();
			}
			
			this.literacies.add(literacy);
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
