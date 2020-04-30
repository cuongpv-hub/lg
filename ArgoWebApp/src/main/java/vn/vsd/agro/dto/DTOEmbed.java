package vn.vsd.agro.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author mely
 */
public abstract class DTOEmbed<K extends Serializable> implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 8634937570421146740L;
    
	private K id;
    private boolean active;

    public DTOEmbed() {
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
    
	public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
