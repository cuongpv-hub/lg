package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.ProvinceEmbed;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "District")
public class District extends POSearchable<ObjectId> {
	/**
	 * 
	 */
    private static final long serialVersionUID = 2788766369237232976L;

    public static final String COLUMNNAME_CODE = "code";
    public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_PROVINCE = "province";
    public static final String COLUMNNAME_PROVINCE_ID = COLUMNNAME_PROVINCE + ".id";
    
    private String code;
    private String name;
    private ProvinceEmbed province;
    
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

	public ProvinceEmbed getProvince() {
		return province;
	}

	public void setProvince(ProvinceEmbed province) {
		this.province = province;
	}

	@Override
    public String[] getSearchValues() {
		List<String> values = new ArrayList<>();
		values.add(code);
		values.add(name);
		
		if (this.province != null) {
			values.add(province.getName());
			
			if (this.province.getCountry() != null) {
				values.add(province.getCountry().getName());
			}
		}
		
		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);
		
		return searchValues;
    }
}
