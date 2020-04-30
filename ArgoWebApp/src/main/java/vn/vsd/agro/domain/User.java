package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.util.RoleUtils;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "User")
public class User extends POSearchable<ObjectId>
{
	/**
	 * 
	 */
    private static final long serialVersionUID = -3805728159701372974L;
    
    public static final String SEARCH_SEPARATOR_NAME = ":Name:";
    public static final String SEARCH_SEPARATOR_EMAIL = ":Email:";
    public static final String SEARCH_SEPARATOR_COMPANY_PHONE = ":Phone1:";
    public static final String SEARCH_SEPARATOR_HOME_PHONE = ":Phone2:";
    public static final String SEARCH_SEPARATOR_MOBILE_PHONE = ":Mobile:";
    public static final String SEARCH_SEPARATOR_FAX = ":Fax:";
    public static final String SEARCH_SEPARATOR_ADDRESS = ":Address:";
    public static final String SEARCH_SEPARATOR_LAST = ":";
    
	public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_PASSWORD = "password";
    
    public static final String COLUMNNAME_EMAIL = "email";
    public static final String COLUMNNAME_COMPANY_PHONE = "companyPhone";
    public static final String COLUMNNAME_HOME_PHONE = "homePhone";
    public static final String COLUMNNAME_MOBILE_PHONE = "mobilePhone";
    public static final String COLUMNNAME_FAX = "fax";
    
    public static final String COLUMNNAME_ADDRESS = "address";
    public static final String COLUMNNAME_LOCATION = "location";
    public static final String COLUMNNAME_LOCATION_ID = COLUMNNAME_LOCATION + ".id";
    public static final String COLUMNNAME_DISTRICT = COLUMNNAME_LOCATION + ".district";
    public static final String COLUMNNAME_DISTRICT_ID = COLUMNNAME_DISTRICT + ".id";
    public static final String COLUMNNAME_PROVINCE = COLUMNNAME_DISTRICT + ".province";
    public static final String COLUMNNAME_PROVINCE_ID = COLUMNNAME_PROVINCE + ".id";
    public static final String COLUMNNAME_COUNTRY = COLUMNNAME_PROVINCE + ".country";
    public static final String COLUMNNAME_COUNTRY_ID = COLUMNNAME_COUNTRY + ".id";
    
    public static final String COLUMNNAME_ROLES = "roles";
    public static final String COLUMNNAME_CURRENT_ORG_ID = "currentOrgId";
    public static final String COLUMNNAME_ACCESS_ORGS = "accessOrgs";
    public static final String COLUMNNAME_APPROVE_ORGS = "approveOrgs";
    
    public static final String COLUMNNAME_ACCESS_TOKEN = "accessToken";
    public static final String COLUMNNAME_VALID_TOKEN = "validToken";
    
    public static final String COLUMNNAME_AVATAR = "avatar";
    
    private String name;
    private String password;
    
    // Role => Auto approve own document
    private Map<String, Boolean> roles;
    
    // Can access to organizations
    private List<ObjectId> accessOrgs;
    
    // Can approve data of organizations
    private List<ObjectId> approveOrgs;
    
    private ObjectId currentOrgId;
    
    // Login token
    private String accessToken;
    
    // Use for active after registering or when forget password
    private String validToken;
    
    private String avatar;
    
    private String companyPhone;
	private String homePhone;
	private String mobilePhone;
	private String fax;
	private String email;
	
	private String address;
    private LocationEmbed location;
    
    private volatile UserExtension userExtension;
    
    public User() {
        super();

        this.name = null;
        this.email = null;
        this.password = null;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, Boolean> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}

	public void addRole(String role, boolean autoApprove) {
		if (role != null) {
			if (this.roles == null) {
				this.roles = new HashMap<String, Boolean>();
			}
			
			this.roles.put(role, autoApprove);
		}
	}
	
	public void clearRole() {
		this.roles = null;
	}
	
	public void clearRegistrableRoles() {
		if (this.roles != null && !this.roles.isEmpty()) {
			for (String role : RoleUtils.REGISTRABLE_ROLES) {
				this.roles.remove(role);
			}
		}
	}
	
    public List<ObjectId> getAccessOrgs() {
		return accessOrgs;
	}

	public void setAccessOrgs(List<ObjectId> accessOrgs) {
		this.accessOrgs = accessOrgs;
	}

	public void addAccessOrg(ObjectId accessOrg) {
		if (accessOrg != null) {
	    	if (this.accessOrgs == null) {
	    		this.accessOrgs = new ArrayList<ObjectId>();
	    	}
	    	
	        this.accessOrgs.add(accessOrg);
		}
    }
	
	public void clearAccessOrgs() {
		this.accessOrgs = null;
	}
	
	public List<ObjectId> getApproveOrgs() {
		return approveOrgs;
	}

	public void setApproveOrgs(List<ObjectId> approveOrgs) {
		this.approveOrgs = approveOrgs;
	}

	public void addApproveOrg(ObjectId approveOrgId) {
		if (approveOrgId != null) {
	    	if (this.approveOrgs == null) {
	    		this.approveOrgs = new ArrayList<ObjectId>();
	    	}
	    	
	        this.approveOrgs.add(approveOrgId);
		}
    }
	
	public void clearApproveOrgs() {
		this.approveOrgs = null;
	}
	
	public boolean canApproveOrg(ObjectId orgId) {
		/*if (this.approveOrgs == null || orgId == null) {
			return false;
		}*/
		return true;
		//return this.approveOrgs.contains(orgId);
    }
	
	public ObjectId getCurrentOrgId() {
    	if (this.accessOrgs == null || this.accessOrgs.isEmpty()) {
    		return null;
    	}
    	
    	if (this.currentOrgId != null) {
	    	for (ObjectId value : this.accessOrgs) {
	    		if (value != null && value.equals(this.currentOrgId)) {
	    			return this.currentOrgId;
	    		}
	    	}
    	}
    	
		return this.accessOrgs.get(0);
	}
	
	public void setCurrentOrgId(ObjectId currentOrgId) {
		this.currentOrgId = currentOrgId;
	}
    
    public String currentOrgIdAsString() {
    	ObjectId value = this.getCurrentOrgId();
    	if (value == null)
    		return null;
    	
    	return value.toString();
    }
    
    public List<String> accessOrgAsStrings() {
    	List<String> values = new ArrayList<>();
    	if (this.accessOrgs != null) {
    		for (ObjectId value : this.accessOrgs) {
    			if (value != null) {
    				values.add(value.toString());
    			}
    		}
    	}
    	
    	return values;
    }
    
    public boolean isAdministrator() {
    	return this.haveRole(RoleUtils.ROLE_ADMIN);
    }

    public boolean isDepartmentAdministrator() {
    	return this.haveRole(RoleUtils.ROLE_DEPARTMENT_ADMIN);
    }
    
    public boolean isAdmin() {
    	return this.haveRole(RoleUtils.ROLE_ADMIN)
    			|| this.haveRole(RoleUtils.ROLE_DEPARTMENT_ADMIN);
    }
    
    public boolean isCompany() {
    	return this.haveRole(RoleUtils.ROLE_COMPANY);
    }
    
    public boolean isFarmer() {
    	return this.haveRole(RoleUtils.ROLE_FARMER);
    }
    
    public boolean isScientist() {
    	return this.haveRole(RoleUtils.ROLE_SCIENTIST);
    }
    
    public boolean haveRole(String role) {
    	if (this.roles != null) {
    		for (String roleValue : this.roles.keySet()) {
    			if (roleValue != null && role.equalsIgnoreCase(roleValue)) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public boolean isInRole(String[] roles) {
    	if (this.roles != null) {
    		for (String roleValue : this.roles.keySet()) {
    			for (String role : roles) {
	    			if (roleValue != null && role.equalsIgnoreCase(roleValue)) {
	    				return true;
	    			}
    			}
    		}
    	}
    	
    	return false;
    }
    
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getValidToken() {
		return validToken;
	}

	public void setValidToken(String validToken) {
		this.validToken = validToken;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationEmbed getLocation() {
		return location;
	}

	public void setLocation(LocationEmbed location) {
		this.location = location;
	}
	
	public UserExtension getUserExtension() {
		return userExtension;
	}

	public void setUserExtension(UserExtension userExtension) {
		this.userExtension = userExtension;
	}

	@Override
    public String[] getSearchValues() {
	    return new String[] { 
	    		SEARCH_SEPARATOR_NAME,
	    		name, 
	    		SEARCH_SEPARATOR_EMAIL,
	    		email, 
	    		SEARCH_SEPARATOR_COMPANY_PHONE,
	    		companyPhone,
	    		SEARCH_SEPARATOR_HOME_PHONE,
	    		homePhone,
	    		SEARCH_SEPARATOR_MOBILE_PHONE,
	    		mobilePhone,
	    		SEARCH_SEPARATOR_FAX,
	    		fax,
	    		SEARCH_SEPARATOR_ADDRESS,
	    		address,
	    		SEARCH_SEPARATOR_LAST
	    	};
    }
}
