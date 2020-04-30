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
@Document(collection = "CommonCategory")
public class CommonCategory extends POSearchable<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3983761080615087609L;

	public static final String COLUMNNAME_TYPE = "type";
	public static final String COLUMNNAME_CODE = "code";
	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_INDEX = "index";
	public static final String COLUMNNAME_DEFAULT = "main";
	
	public static final int TYPE_PROJECT_TYPE = 0;
	public static final int TYPE_PRODUCT_TYPE = 1;
	public static final int TYPE_SQUARE_UOM = 2;
	public static final int TYPE_MONEY_UOM = 3;
	public static final int TYPE_VOLUME_UOM = 4;
	
	public static final int TYPE_LAND_TYPE = 5;
	public static final int TYPE_LAND_PURPOSE = 6;
	public static final int TYPE_LAND_NEAR = 7;
	
	public static final int TYPE_COMPANY_TYPE = 8;
	public static final int TYPE_COMPANY_FIELD = 11;
	
	public static final int TYPE_SCIENTIST_MAJOR = 9;
	public static final int TYPE_LITERACY = 12;	// Trinh do hoc van
	public static final int TYPE_NEWS_TYPE = 10;
	public static final int TYPE_ARTICLE_TYPE = 15;
	
	public static final int TYPE_LAND_COOPERATE = 13;
	public static final int TYPE_PRODUCT_COOPERATE = 14;
	public static final int TYPE_COMPANY_COOPERATE = 16;
	public static final int TYPE_FARMER_COOPERATE = 17;
	public static final int TYPE_SCIENTIST_COOPERATE = 18;
	
	private int type;
	private String code;
	private String name;
	private int index;
	private boolean main;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	@Override
	public String[] getSearchValues() {
		return new String[] { code, name };
	}

}
