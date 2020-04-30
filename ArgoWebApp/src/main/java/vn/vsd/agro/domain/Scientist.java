package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.ScientistMajorEmbed;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.util.StringUtils;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Scientist")
public class Scientist extends UserExtension {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1942057215934635648L;
	
	public static final String SEARCH_SEPARATOR_TITLE = ":Title:";
    public static final String SEARCH_SEPARATOR_POSITION = ":Position:";
    public static final String SEARCH_SEPARATOR_WORKSPACE = ":Workspace:";
    public static final String SEARCH_SEPARATOR_LAST = ":";
    
	public static final String COLUMNNAME_GENDER = "gender";
	public static final String COLUMNNAME_BIRTHDAY = "birthDay";
	public static final String COLUMNNAME_MAJORS = "scientistMajors";
	public static final String COLUMNNAME_MAJOR_ID = COLUMNNAME_MAJORS + ".id";
	public static final String COLUMNNAME_LITERACIES = "literacies";
	public static final String COLUMNNAME_LITERACY_ID = COLUMNNAME_LITERACIES + ".id";
	public static final String COLUMNNAME_TITLE = "title";
	public static final String COLUMNNAME_POSSITION = "position";
	public static final String COLUMNNAME_WORKPLACE = "workplace";

	/**
	 * { @value ConstantUtils#GENDER_MALE, @value
	 * ConstantUtils#GENDER_FEMALE, @value ConstantUtils#GENDER_OTHER }
	 */
	private int gender;
	private SimpleDate birthDay;

	private List<ScientistMajorEmbed> scientistMajors;
	private List<IdCodeNameEmbed> literacies; // Trinh do hoc van
	private String title; // chuc danh
	private String position; // chuc vu
	private String workplace; // Đơn vị công tác

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public SimpleDate getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(SimpleDate birthDay) {
		this.birthDay = birthDay;
	}

	public List<ScientistMajorEmbed> getScientistMajors() {
		return scientistMajors;
	}

	public void setScientistMajors(List<ScientistMajorEmbed> scientistMajors) {
		this.scientistMajors = scientistMajors;
	}

	public void clearScientistMajors() {
		this.scientistMajors = null;
	}
	
	public void addScientistMajor(ScientistMajorEmbed scientistMajor) {
		if (scientistMajor != null) {
			if (this.scientistMajors == null) {
				this.scientistMajors = new ArrayList<>();
			} else {
				for (int i = 0; i < this.scientistMajors.size(); i++) {
					ScientistMajorEmbed major = this.scientistMajors.get(i);
					if (major.getId().equals(scientistMajor.getId())) {
						this.scientistMajors.set(i, scientistMajor);
						return;
					}
				}
			}
			
			this.scientistMajors.add(scientistMajor);
		}
	}
	
	public void addScientistField(ObjectId scientistMajorId, IdCodeNameEmbed scientistField) {
		if (this.scientistMajors != null && scientistMajorId != null && scientistField != null) {
			for (int i = 0; i < this.scientistMajors.size(); i++) {
				ScientistMajorEmbed major = this.scientistMajors.get(i);
				
				if (scientistMajorId.equals(major.getId())) {
					major.addScientistField(scientistField);
					this.scientistMajors.set(i, major);
					
					break;
				}
			}
		}
	}
	
	public void addScientistField(ScientistMajorEmbed scientistMajor, IdCodeNameEmbed scientistField) {
		if (this.scientistMajors != null && scientistMajor != null && scientistField != null) {
			ObjectId scientistMajorId = scientistMajor.getId();
			
			for (int i = 0; i < this.scientistMajors.size(); i++) {
				ScientistMajorEmbed major = this.scientistMajors.get(i);
				
				if (scientistMajorId.equals(major.getId())) {
					major.addScientistField(scientistField);
					this.scientistMajors.set(i, major);
					return;
				}
			}
			
			scientistMajor.addScientistField(scientistField);
			this.scientistMajors.add(scientistMajor);
		}
	}
	
	public void addOtherScientistField(ObjectId scientistMajorId, String scientistFieldName) {
		if (this.scientistMajors != null && scientistMajorId != null 
				&& !StringUtils.isNullOrEmpty(scientistFieldName)) {
			for (int i = 0; i < this.scientistMajors.size(); i++) {
				ScientistMajorEmbed major = this.scientistMajors.get(i);
				
				if (scientistMajorId.equals(major.getId())) {
					major.setFieldOther(scientistFieldName);
					this.scientistMajors.set(i, major);
					
					break;
				}
			}
		}
	}
	
	public void addScientistFieldDescription(ObjectId scientistMajorId, String scientistFieldDescription) {
		if (this.scientistMajors != null && scientistMajorId != null 
				&& !StringUtils.isNullOrEmpty(scientistFieldDescription)) {
			for (int i = 0; i < this.scientistMajors.size(); i++) {
				ScientistMajorEmbed major = this.scientistMajors.get(i);
				
				if (scientistMajorId.equals(major.getId())) {
					major.setFieldDescription(scientistFieldDescription);
					this.scientistMajors.set(i, major);
					
					break;
				}
			}
		}
	}
	
	public List<IdCodeNameEmbed> getLiteracies() {
		return literacies;
	}

	public void setLiteracies(List<IdCodeNameEmbed> literacies) {
		this.literacies = literacies;
	}

	public void clearLiteracies() {
		this.literacies = null;
	}
	
	public void addLiteracy(IdCodeNameEmbed literacy) {
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
	
	@Override
	public String[] getSearchValues() {
		List<String> values = new ArrayList<>();
		
		String[] searchUsers = this.getSearchUsers();
		if (searchUsers != null) {
			for (String searchUser : searchUsers) {
				values.add(searchUser);
			}
		}
		
		values.add(SEARCH_SEPARATOR_TITLE);
		values.add(this.title);
		values.add(SEARCH_SEPARATOR_POSITION);
		values.add(this.position);
		values.add(SEARCH_SEPARATOR_WORKSPACE);
		values.add(this.workplace);
		values.add(SEARCH_SEPARATOR_LAST);
		
		/*if (this.literacies != null) {
			for (IdCodeNameEmbed literacy : this.literacies) {
				if (literacy != null) {
					values.add(literacy.getName());
				}
			}
		}
		
		if (this.scientistMajors != null) {
			for (ScientistMajorEmbed scientistMajor : this.scientistMajors) {
				if (scientistMajor != null) {
					values.add(scientistMajor.getName());
				}
			}
		}*/
		
		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);
		
		return searchValues;
	}
}
