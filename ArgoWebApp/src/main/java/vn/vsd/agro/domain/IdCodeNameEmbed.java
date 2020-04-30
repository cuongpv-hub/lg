package vn.vsd.agro.domain;

import org.bson.types.ObjectId;

public class IdCodeNameEmbed extends IdNameEmbed {
	/**
	 * 
	 */
    private static final long serialVersionUID = 4522442255226736684L;

    private String code;

	public IdCodeNameEmbed() {
	    super();
    }

	public IdCodeNameEmbed(PO<ObjectId> po) {
	    super(po);
    }
	
	public IdCodeNameEmbed(POEmbed<ObjectId> po) {
	    super(po);
    }
	
	public IdCodeNameEmbed(ObjectId id, String code, String name) {
	    super(id, name);
	    
	    this.code = code;
    }
	
	public IdCodeNameEmbed(PO<ObjectId> po, String code, String name) {
	    super(po, name);
	    
	    this.code = code;
    }

	public IdCodeNameEmbed(POEmbed<ObjectId> po, String code, String name) {
	    super(po, name);
	    
	    this.code = code;
    }

	public IdCodeNameEmbed(IdCodeNameEmbed po) {
	    this(po, po.getCode(), po.getName());
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
