package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Location")
public class Location extends POSearchable<ObjectId> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9131288987755881779L;
	
	public static final String COLUMNNAME_CODE = "code";
	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_DISTRICT = "district";
	public static final String COLUMNNAME_DISTRICT_ID = COLUMNNAME_DISTRICT + ".id";

	private String code;
	private String name;
	private DistrictEmbed district;

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

	public DistrictEmbed getDistrict() {
		return district;
	}

	public void setDistrict(DistrictEmbed district) {
		this.district = district;
	}

	@Override
	public String[] getSearchValues() {
		List<String> values = new ArrayList<>();
		values.add(this.code);
		values.add(this.name);
		
		if (this.district != null) {
			values.add(this.district.getName());
			
			ProvinceEmbed province = this.district.getProvince();
			if (province != null) {
				values.add(province.getName());
				
				CountryEmbed country = province.getCountry();
				if (country != null) {
					values.add(country.getName());
				}
			}
		}
		
		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);
		
		return searchValues;
	}

}
