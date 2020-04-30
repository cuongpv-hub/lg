package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.District;
import vn.vsd.agro.domain.POEmbed;

public class DistrictEmbed extends POEmbed<ObjectId> {
    /**
     * 
     */
    private static final long serialVersionUID = -829222091786203533L;
    
    private String code;
    private String name;
    private ProvinceEmbed province;

    public DistrictEmbed() {
        super();
    }

    public DistrictEmbed(District po) {
        super(po);
        
        if (po != null) {
	    	this.code = po.getCode();
	    	this.name = po.getName();
	    	this.province = po.getProvince();
	    }
    }

    public DistrictEmbed(DistrictEmbed po) {
    	super(po);
    	
    	if (po != null) {
            this.province = po.getProvince();
        }
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

	public ProvinceEmbed getProvince() {
        return province;
    }

    public void setProvince(ProvinceEmbed province) {
        this.province = province;
    }
}
