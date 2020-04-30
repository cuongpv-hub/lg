package vn.vsd.agro.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.context.IUser;
import vn.vsd.agro.domain.annotation.ClientInclude;
import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.CreatedOnly;
import vn.vsd.agro.domain.annotation.OrgInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.util.StringUtils;

/**
 * Persistent Object
 * 
 * @author mely
 */
public abstract class PO<K extends Serializable> implements Persistable<K> {

    private static final long serialVersionUID = 3498632065144170387L;
    
    public static final String COLUMNNAME_CLIENT_ID = "clientId";
    public static final String COLUMNNAME_ORG_ID = "orgId";
    public static final String COLUMNNAME_ACTIVE = "active";
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_CREATED = "created";
    public static final String COLUMNNAME_CREATED_BY = "createdBy";
    public static final String COLUMNNAME_CREATED_BY_ID = COLUMNNAME_CREATED_BY + ".id";
    public static final String COLUMNNAME_MODIFIED = "modified";
    public static final String COLUMNNAME_MODIFIED_BY = "modifiedBy";
    public static final String COLUMNNAME_MODIFIED_BY_ID = COLUMNNAME_MODIFIED_BY + ".id";
    
    @Id
    private K id;

    private K clientId;

    private K orgId;
    
    private boolean active;

    private IUser<K> createdBy;
    
    private K created;
    
    private IUser<K> modifiedBy;
    
    private K modified;
    
    protected PO() {
    	this.id = null;
        this.active = true;
    }
    
    public PO(PO<K> po) {
    	this();
    	
    	if (po != null)
    	{
    		this.clientId = po.getClientId();
    		this.id = po.getId();
    		this.active = po.isActive();
    		this.modified = po.getModified();
    	}
    }
    
    public K getClientId() {
        return clientId;
    }

    @SuppressWarnings("unchecked")
    public void setClientId(K clientId) {
        if (this.clientId == null || !PO.isClientRootFixed(this.getClass())) {
            this.clientId = clientId;
        }
    }
    
    public K getOrgId() {
		return orgId;
	}

	@SuppressWarnings("unchecked")
    public void setOrgId(K orgId) {
		if (this.orgId == null || !PO.isOrgRootFixed(this.getClass())) {
            this.orgId = orgId;
        }
	}

	@Override
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

	public IUser<K> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(IUser<K> createdBy) {
		this.createdBy = createdBy;
	}

	public K getCreated() {
		return created;
	}

	public void setCreated(K created) {
		this.created = created;
	}

	public IUser<K> getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(IUser<K> modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public K getModified() {
		if (modified == null)
			return id;
		
		return modified;
	}

	public void setModified(K modified) {
		this.modified = modified;
	}

	@Override
	public boolean isNew() {
        return (this.id == null);
    }
	
	public String idAsString() {
		return this.getId().toString();
	}
	
	public String clientAsString() {
		return this.getClientId().toString();
	}
	
	public String orgAsString() {
		return this.getOrgId().toString();
	}
	
	/***************** OBJECT'S METHODS *************************/
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass().equals(this.getClass())) {
            PO<K> po = (PO<K>) obj;
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

    /******************* STATIC METHODS *****************************/
    public static <D extends PO<K>, K extends Serializable> boolean isClientRootFixed(Class<D> clazz) {
        return clazz.isAnnotationPresent(ClientRootFixed.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isIncludeClientRoot(Class<D> clazz) {
        return clazz.isAnnotationPresent(ClientRootInclude.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isOrgRootFixed(Class<D> clazz) {
        return clazz.isAnnotationPresent(OrgRootFixed.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isIncludeOrgRoot(Class<D> clazz) {
        return clazz.isAnnotationPresent(OrgRootInclude.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isIncludeClient(Class<D> clazz) {
        return clazz.isAnnotationPresent(ClientInclude.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isIncludeOrg(Class<D> clazz) {
        return clazz.isAnnotationPresent(OrgInclude.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> boolean isCreatedOnly(Class<D> clazz) {
        return clazz.isAnnotationPresent(CreatedOnly.class);
    }
    
    public static <D extends PO<K>, K extends Serializable> String getTableName(Class<D> clazz) {
    	Document doc = clazz.getAnnotation(Document.class);
    	if (doc == null) {
    		return clazz.getName();
    	}
    	
    	String tableName = doc.collection();
    	if (StringUtils.isNullOrEmpty(tableName)) {
    		return clazz.getName();
    	}
    	
        return tableName;
    }
    
    @SuppressWarnings("unchecked")
    public static <K extends Serializable> void copy(PO<K> source, PO<K> destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("param is null");
        }
        
        destination.setId(source.getId());
        destination.setActive(source.isActive());
        
        if (!PO.isClientRootFixed(destination.getClass())) {
	        destination.setClientId(source.getClientId());
		}
        
        if (!PO.isOrgRootFixed(destination.getClass())) {
	        destination.setOrgId(source.getOrgId());
		}
    }
}
