package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.entity.SimpleDate;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Farmer")
public class Farmer extends UserExtension {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1634736779558572488L;

	public static final String COLUMNNAME_BIRTHDAY = "birthDay";
	public static final String COLUMNNAME_GENDER = "gender";

	/** { @value ConstantUtils#GENDER_MALE, @value ConstantUtils#GENDER_FEMALE, @value ConstantUtils#GENDER_OTHER } */
	private int gender;
	private SimpleDate birthDay;
	
	public SimpleDate getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(SimpleDate birthDay) {
		this.birthDay = birthDay;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
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
		
		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);
		
		return searchValues;
	}
}
