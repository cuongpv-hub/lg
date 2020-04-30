package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.vsd.agro.util.StringUtils;

/**
 * @author mely
 */
public class UserDto extends DTO<String> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 4040226399553276435L;
    
    private String name;
    private String email;
    private String companyPhone;
    private String homePhone;
    private String mobilePhone;
    private String address;
    private LocationDto location;
    
    // Role => Auto approve own document
    private Map<String, Boolean> roles;
    private List<String> accessOrgs;
	
    public UserDto() {
        super();
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public Map<String, Boolean> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}

	public List<String> getAccessOrgs() {
		return accessOrgs;
	}

	public void setAccessOrgs(List<String> accessOrgs) {
		this.accessOrgs = accessOrgs;
	}
	
	public void addAccessOrg(String accessOrg) {
		if (!StringUtils.isNullOrEmpty(accessOrg, true)) {
			if (this.accessOrgs == null) {
				this.accessOrgs = new ArrayList<String>();
			}
			
			this.accessOrgs.add(accessOrg);
		}
	}
}
