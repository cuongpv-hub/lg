package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.POEmbed;
import vn.vsd.agro.domain.Province;


public class ProvinceEmbed extends POEmbed<ObjectId> {
    /**
     * 
     */
    private static final long serialVersionUID = -3760034367115661319L;

    private String code;
    private String name;
    private CountryEmbed country;
    
    public ProvinceEmbed() {
        super();
    }

    public ProvinceEmbed(Province po) {
        super(po);
        
        if (po != null) {
	    	this.code = po.getCode();
	    	this.name = po.getName();
	    	this.country = po.getCountry();
	    }
    }
    
    public ProvinceEmbed(ProvinceEmbed po) {
        super(po);
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

	public CountryEmbed getCountry() {
		return country;
	}

	public void setCountry(CountryEmbed country) {
		this.country = country;
	}
}
