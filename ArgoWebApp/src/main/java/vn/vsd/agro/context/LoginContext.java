package vn.vsd.agro.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class LoginContext implements IContext<String> {
	/**
	 * 
	 */
    private static final long serialVersionUID = 995612937254280312L;
    
    private static final String CONTEXT_ROOT_ID = "ROOT";
    
    private static UserInfo<String> ROOT_USER_INFO = new UserInfo<String>(
    		"Root", "Root", CONTEXT_ROOT_ID, null);
    
    public static LoginContext ROOT_CONTEXT = new LoginContext(CONTEXT_ROOT_ID, 
    		"Client Domain", CONTEXT_ROOT_ID, CONTEXT_ROOT_ID, ROOT_USER_INFO);
    
	private String clientId;
	private String clientName;
	private UserInfo<String> userInfo;
	
	private String orgId;
	private String userId;
	
	private IContext<?> dbContext;
	
	public LoginContext(String clientId, String clientName,  
			String orgId, String userId, UserInfo<String> userInfo) {
	    super();
	    
	    this.clientId = clientId;
	    this.clientName = clientName;
	    this.orgId = orgId;
	    this.userId = userId;
	    this.userInfo = userInfo;
    }
	
    @Override
    public String getRootId() {
        return CONTEXT_ROOT_ID;
    }

    @Override
    public String newId() {
        return "" + System.currentTimeMillis();
    }

    @Override
    public String getModified() {
        return "" + System.currentTimeMillis();
    }

    @Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientName() {
		return clientName;
	}

	@Override
	public UserInfo<String> getUserInfo() {
		return userInfo;
	}
	
	@Override
	public String getOrgId() {
		return orgId;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
    public IContext<String> getRootContext() {
	    return ROOT_CONTEXT;
    }
	
	@Override
	public boolean hasRole(String...roles) {
		if (this.userInfo != null) {
			Collection<String> userRoles = this.userInfo.getRoles();
			
			if (userRoles == null || userRoles.isEmpty() || roles == null || roles.length == 0) {
	            return false;
	        }
	        
			for (String role : roles) {
				role = role.trim().toUpperCase();
				if (userRoles.contains(role))
					return true;
	        }
		}
		
        return false;
	}

	@Override
	public String parse(Object value, String defaultValue) {
		if (value == null)
			return defaultValue;
		
		return value.toString();
	}

	@Override
    public List<String> parseMulti(Collection<?> values, boolean ignoreEmptyValue) {
	    List<String> retValues = new ArrayList<String>();
	    
	    if (values != null && values.size() > 0) {
	    	for (Object value : values) {
	    		String parseValue = this.parse(value, null);
	    		if (!ignoreEmptyValue || (parseValue != null && !"".equals(parseValue))) {
	    			retValues.add(parseValue);
	    		}
	    	}
	    }
	    
	    return retValues;
    }
	
	protected IContext<?> getDbContext() {
		return dbContext;
	}

	protected void setDbContext(IContext<?> dbContext) {
		this.dbContext = dbContext;
	}

	@Override
	public IContext<?> getDbContext(IContext<String> loginContext, IClient<?> client)
	{
		return dbContext;
	}
	
	@Override
    public boolean equals(String id1, String id2) {
	    if (id1 == null) {
	    	return id2 == null;
	    }
	    
	    return id1.equals(id2);
    }

	@Override
    public boolean in(String id, Collection<String> values) {
	    if (id == null || values == null || values.isEmpty()) {
	    	return false;
	    }
	    
	    return values.contains(id);
    }

	@Override
    public boolean isRoot(String id)
	{
	    return CONTEXT_ROOT_ID.equalsIgnoreCase(id);
    }

	@Override
    public boolean isNullOrEmpty(String id) {
	    return StringUtils.isNullOrEmpty(id);
    }

	@Override
    public IUser<String> getUser() {
	    return null;
    }
}
