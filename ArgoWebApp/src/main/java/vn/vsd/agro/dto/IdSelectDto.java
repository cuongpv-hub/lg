package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.List;

public class IdSelectDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4892991898559474422L;
	
	public static final int LINK_TYPE_PRODUCT = 0;
    public static final int LINK_TYPE_LAND = 1;
    public static final int LINK_TYPE_SCIENTIST = 2;
    public static final int LINK_TYPE_PROJECT = 3;
    
	private int type;
	private List<String> ids;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}
