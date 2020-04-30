package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.Country;
import vn.vsd.agro.domain.POEmbed;

public class CountryEmbed extends POEmbed<ObjectId> {
    /**
     * 
     */
    private static final long serialVersionUID = -3760034367115661319L;

    private String code;
    private String name;
    
    public CountryEmbed() {
        super();
    }

    public CountryEmbed(Country po) {
	    super(po);
	    
	    if (po != null) {
	    	this.code = po.getCode();
	    	this.name = po.getName();
	    }
    }

	public CountryEmbed(CountryEmbed po) {
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
}
