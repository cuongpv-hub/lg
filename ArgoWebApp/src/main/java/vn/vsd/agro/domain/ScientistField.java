package vn.vsd.agro.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "ScientistField")
public class ScientistField extends POSearchable<ObjectId>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4339320358242714807L;
	
	public static final String COLUMNNAME_CODE = "code";
    public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_INDEX = "index";
    public static final String COLUMNNAME_MAJOR = "major";    
    public static final String COLUMNNAME_MAJOR_ID = COLUMNNAME_MAJOR + ".id";
    public static final String COLUMNNAME_MAJOR_NAME = COLUMNNAME_MAJOR + ".name";
	
	private String code;
	private String name;	
	private int index;
	private IdCodeNameEmbed major;
	
	public IdCodeNameEmbed getMajor() {
		return major;
	}

	public void setMajor(IdCodeNameEmbed major) {
		this.major = major;
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

	@Override
	public String[] getSearchValues() {
		return new String[] { code, name };
	}
}
