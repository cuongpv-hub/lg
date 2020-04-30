package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.POEmbed;

public class LocationEmbed extends POEmbed<ObjectId> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2099560262149599537L;
	
	private String code;
    private String name;
    private DistrictEmbed district;

    public LocationEmbed() {
        super();
    }

    public LocationEmbed(Location po) {
        super(po);
        
        if (po != null) {
	    	this.code = po.getCode();
	    	this.name = po.getName();
	    	this.district = po.getDistrict();
	    }
    }

    public LocationEmbed(LocationEmbed po) {
    	super(po);
    	
    	if (po != null) {
            this.district = po.getDistrict();
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

	public DistrictEmbed getDistrict() {
		return district;
	}

	public void setDistrict(DistrictEmbed district) {
		this.district = district;
	}

}
