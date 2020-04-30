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
@Document(collection = "SystemConfig")
public class SystemConfig extends PO<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7534532933854989373L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
