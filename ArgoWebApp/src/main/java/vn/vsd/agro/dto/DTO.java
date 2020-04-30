package vn.vsd.agro.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author mely
 */
public abstract class DTO<K extends Serializable> implements Serializable {

    private static final long serialVersionUID = -862151765440311950L;
    
    private K id;
    //private K clientId;
    //private K orgId;
    private boolean active;

    public DTO() {
        super();

        this.id = null;
        this.active = true;
    }
    
    @XmlJavaTypeAdapter(SerializableAdapter.class)
    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
    
    /*
    @XmlJavaTypeAdapter(SerializableAdapter.class)
    public K getClientId() {
		return clientId;
	}

	public void setClientId(K clientId) {
		this.clientId = clientId;
	}
    
	@XmlJavaTypeAdapter(SerializableAdapter.class)
	public K getOrgId() {
		return orgId;
	}

	public void setOrgId(K orgId) {
		this.orgId = orgId;
	}
	*/
    
	public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
