package vn.vsd.agro.context;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class UserInfo<K extends Serializable> implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = 3965185360084450118L;
    
	private String username;
    private String fullname;
    
    private Collection<K> accessOrgs;
    private Collection<String> roles;
    
    public UserInfo() {
    	super();
    }
    
	public UserInfo(String username, String fullname, 
			Collection<K> accessOrgs, Collection<String> roles) {
		this();
		
	    this.username = username;
	    this.fullname = fullname;
	    this.accessOrgs = accessOrgs;
	    this.roles = roles;
    }

	public UserInfo(String username, String fullname, 
			K orgId, String roleCode) {
		this();
		
	    this.username = username;
	    this.fullname = fullname;
	    
	    if (orgId != null) {
	    	this.accessOrgs = new HashSet<K>();
	    	this.accessOrgs.add(orgId);
	    }
	    
	    if (roleCode != null && roleCode.trim().length() > 0) {
	        this.roles = new HashSet<String>();
		    this.roles.add(roleCode);
        }
    }
	
	public <T extends Serializable> UserInfo(UserInfo<T> userInfo, IContext<K> context) {
    	this();
    	
    	if (userInfo != null) {
	    	this.username = userInfo.getUsername();
	    	this.fullname = userInfo.getFullname();
			
	    	Collection<T> accessOrgs = userInfo.getAccessOrgs();
			if (accessOrgs != null && !accessOrgs.isEmpty()) {
				this.accessOrgs = new HashSet<K>();
				
				Iterator<T> iteratorAccessOrgs = accessOrgs.iterator();
				while (iteratorAccessOrgs.hasNext()) {
					K orgId = context.parse(iteratorAccessOrgs.next(), null);
					if (orgId != null) {
						this.accessOrgs.add(orgId);
					}
				}
			}
			
			Collection<String> userRoles = userInfo.getRoles();
			if (userRoles != null && !userRoles.isEmpty()) {
				this.roles = new HashSet<String>(userRoles);
			}
    	}
    }
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Collection<K> getAccessOrgs() {
		return accessOrgs;
	}

	public void setAccessOrgs(Collection<K> accessOrgs) {
		this.accessOrgs = accessOrgs;
	}

	public void addAccessOrg(K orgId) {
		if (orgId != null) {
			if (this.accessOrgs == null) {
				this.accessOrgs = new HashSet<K>();
			}
			
			this.accessOrgs.add(orgId);
		}
	}
	
	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String code) {
		if (code != null && code.trim().length() > 0) {
			if (this.roles == null) {
				this.roles = new HashSet<String>();
			}
			
			this.roles.add(code);
		}
	}
}
