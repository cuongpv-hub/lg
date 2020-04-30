package vn.vsd.agro.dto;

import java.io.Serializable;

public class IdDto implements Serializable {

    private static final long serialVersionUID = 5042675909233566226L;
    
    private String id;
    
    public IdDto() {
		super();
	}

	public IdDto(String id) {
        super();
        
        this.id = id;
    }

    public IdDto(int id) {
        super();
        
        this.id = String.valueOf(id);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	@Override
	public String toString() {
		return id;
	}
}
