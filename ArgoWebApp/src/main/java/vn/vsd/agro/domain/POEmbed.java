package vn.vsd.agro.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

/**
 * Persistent Embed Object
 * 
 * @author mely
 */
public abstract class POEmbed<K extends Serializable> implements Persistable<K> {
    /**
     * 
     */
    private static final long serialVersionUID = -4794016198702346757L;

    public static final String COLUMNNAME_ID = "id";
    
    @Id
    private K id;
    
    public POEmbed() {
        super();
    }

    public POEmbed(PO<K> po) {
        this();
        
        if (po != null) {
            this.id = po.getId();
        }
    }
    
    public POEmbed(POEmbed<K> po) {
        this();
        
        if (po != null) {
            this.id = po.getId();
        }
    }
    
    @Override
    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
    
    @Override
    public boolean isNew() {
        return (this.id == null);
    }
    
    public String idAsString() {
    	if (this.id == null) {
    		return null;
    	}
    	
		return this.id.toString();
	}
    
    /***************** OBJECT'S METHODS *************************/
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass().equals(this.getClass())) {
            POEmbed<K> po = (POEmbed<K>) obj;
            if (po.getId() != null && this.getId() != null) {
                return po.getId().equals(this.getId());
            }

            return super.equals(obj);
        }

        return false;
    }
    
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName()).append("=").append(getId()).toString();
    }
}
