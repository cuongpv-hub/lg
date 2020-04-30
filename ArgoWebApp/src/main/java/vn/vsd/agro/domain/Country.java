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
@Document(collection = "Country")
public class Country extends POSearchable<ObjectId> {
	/**
	 * 
	 */
    private static final long serialVersionUID = -847175402162139209L;
    
    public static final String COLUMNNAME_CODE = "code";
    public static final String COLUMNNAME_NAME = "name";
    
    private String code;
    private String name;

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

	@Override
    public String[] getSearchValues() {
	    return new String[] { code, name };
    }
}
