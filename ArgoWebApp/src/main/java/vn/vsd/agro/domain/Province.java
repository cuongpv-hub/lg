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

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Province")
public class Province extends POSearchable<ObjectId> {
    /**
     * 
     */
    private static final long serialVersionUID = -5240720669322446762L;
    
    public static final String COLUMNNAME_CODE = "code";
    public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_COUNTRY = "country";
    public static final String COLUMNNAME_COUNTRY_ID = COLUMNNAME_COUNTRY + ".id";
    
    private String code;
    private String name;
    private CountryEmbed country;

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

    public CountryEmbed getCountry() {
        return country;
    }

    public void setCountry(CountryEmbed country) {
        this.country = country;
    }

    @Override
    public String[] getSearchValues() {
    	List<String> values = new ArrayList<>();
		values.add(code);
		values.add(name);
		
		if (this.country != null) {
			values.add(country.getName());
		}
		
		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);
		
		return searchValues;
    }
}
