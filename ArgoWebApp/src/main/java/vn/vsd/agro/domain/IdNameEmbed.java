package vn.vsd.agro.domain;

import org.bson.types.ObjectId;

public class IdNameEmbed extends POEmbed<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8967767073396030229L;

	private String name;

	public IdNameEmbed() {
		super();
	}

	public IdNameEmbed(PO<ObjectId> po) {
		super(po);
	}
	
	public IdNameEmbed(POEmbed<ObjectId> po) {
		super(po);
	}
	
	public IdNameEmbed(ObjectId id, String name) {
		super();
		
		this.setId(id);
		this.name = name;
	}
	
	public IdNameEmbed(PO<ObjectId> po, String name) {
		super(po);
		
		this.name = name;
	}

	public IdNameEmbed(POEmbed<ObjectId> po, String name) {
		super(po);
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
