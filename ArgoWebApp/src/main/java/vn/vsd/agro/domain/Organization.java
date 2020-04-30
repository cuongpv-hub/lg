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
@Document(collection = "Organization")
public class Organization extends POSearchable<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -121225289862434704L;
	
	public static final String COLUMNNAME_CODE = "code";
	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_INDEX = "index";
	public static final String COLUMNNAME_PARENT_ID = "parentId";
	
	private String code;
	private String name;
	private int index;
	
	private ObjectId parentId;
	
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

	public ObjectId getParentId() {
		return parentId;
	}

	public void setParentId(ObjectId parentId) {
		this.parentId = parentId;
	}

	@Override
	public String[] getSearchValues() {
		return new String[] { code, name };
	}
}
