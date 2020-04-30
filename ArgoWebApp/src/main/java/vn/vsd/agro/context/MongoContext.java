package vn.vsd.agro.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.embed.UserEmbed;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;

public class MongoContext implements IContext<ObjectId>, DbContext
{
	/**
	 * 
	 */
    private static final long serialVersionUID = -3300274909913016195L;

	public static final ObjectId CONTEXT_ROOT_ID = ObjectId.createFromLegacyFormat(0, 0, 0);
    
	private static UserInfo<ObjectId> ROOT_USER_INFO = new UserInfo<ObjectId>(
    		"Root", "Root", CONTEXT_ROOT_ID, RoleUtils.ROLE_SUPER_ADMIN);
    
    private static UserInfo<ObjectId> ADMIN_USER_INFO = new UserInfo<ObjectId>(
    		"Admin", "Administrator", CONTEXT_ROOT_ID, RoleUtils.ROLE_ADMIN);
    
    public static MongoContext ROOT_CONTEXT = new MongoContext(MongoContext.CONTEXT_ROOT_ID, 
    		"Horeca", CONTEXT_ROOT_ID, CONTEXT_ROOT_ID, ROOT_USER_INFO);
    
	private ObjectId clientId;
	private String clientName;
	private UserInfo<ObjectId> userInfo;
	private ObjectId orgId;
	private ObjectId userId;
	
	public MongoContext() {
	    super();
    }
	
	public MongoContext(ObjectId clientId, String clientName,  
			ObjectId orgId, ObjectId userId, UserInfo<ObjectId> userInfo) {
	    super();
	    
	    this.clientId = clientId;
	    this.clientName = clientName;
	    this.orgId = orgId;
	    this.userId = userId;
	    this.userInfo = userInfo;
    }
	
    @Override
    public ObjectId getRootId() {
        return CONTEXT_ROOT_ID;
    }

    @Override
    public ObjectId newId() {
        return new ObjectId();
    }

    @Override
    public ObjectId getModified() {
        return new ObjectId();
    }
    
	@Override
	public ObjectId getClientId() {
		return clientId;
	}
	
	public void setClientId(ObjectId clientId) {
		this.clientId = clientId;
	}
	
	@Override
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public UserInfo<ObjectId> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo<ObjectId> userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public ObjectId getOrgId() {
		return orgId;
	}

	public void setOrgId(ObjectId orgId) {
		this.orgId = orgId;
	}

	@Override
	public ObjectId getUserId() {
		return userId;
	}
	
	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	
	@Override
    public IContext<ObjectId> getRootContext() {
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
	public ObjectId parse(Object value, ObjectId defaultValue) {
		if (value == null)
			return defaultValue;
		
		if (value instanceof ObjectId)
			return (ObjectId)value;
		
		if (ObjectId.isValid(value.toString())) {
			return new ObjectId(value.toString());
		}
		
		return defaultValue;
	}

	@Override
    public List<ObjectId> parseMulti(Collection<?> values, boolean ignoreEmptyValue) {
		List<ObjectId> retValues = new ArrayList<ObjectId>();
	    
	    if (values != null && values.size() > 0) {
	    	for (Object value : values) {
	    		ObjectId parseValue = this.parse(value, null);
	    		if (!ignoreEmptyValue || parseValue != null) {
	    			retValues.add(parseValue);
	    		}
	    	}
	    }
	    
	    return retValues;
    }
	
	@Override
    public boolean equals(ObjectId id1, ObjectId id2) {
	    if (id1 == null) {
	    	return id2 == null;
	    }
	    
	    return id1.equals(id2);
    }

	@Override
    public boolean in(ObjectId id, Collection<ObjectId> values) {
	    if (id == null || values == null || values.isEmpty()) {
	    	return false;
	    }
	    
	    return values.contains(id);
    }
	
	@Override
	public IContext<ObjectId> getDbContext(IContext<String> context, IClient<?> client)
	{
		if (context == null)
		{
			if (client == null)
			{
				return new MongoContext();
			}
			
			return new MongoContext((ObjectId)client.getId(), client.getName(),  
					CONTEXT_ROOT_ID, CONTEXT_ROOT_ID, ADMIN_USER_INFO);
		}
		
		IContext<?> dbContext = context.getDbContext(context, client);
		if (dbContext != null && dbContext instanceof MongoContext)
		{
			return (MongoContext)dbContext;
		}
		
		dbContext = new MongoContext();
		
		if (client != null)
		{
			((MongoContext)dbContext).setClientId((ObjectId)client.getId());
			((MongoContext)dbContext).setClientName(client.getName());
		}
		else
		{
			if (!StringUtils.isNullOrEmpty(context.getClientId()))
			{
				ObjectId clientId = ((MongoContext)dbContext).parse(context.getClientId(), null);
				((MongoContext)dbContext).setClientId(clientId);
			}
			
			((MongoContext)dbContext).setClientName(context.getClientName());
		}
		
		if (!StringUtils.isNullOrEmpty(context.getOrgId()))
		{
			ObjectId orgId = ((MongoContext)dbContext).parse(context.getOrgId(), null);
			((MongoContext)dbContext).setOrgId(orgId);
		}
		
		if (!StringUtils.isNullOrEmpty(context.getUserId()))
		{
			ObjectId userId = ((MongoContext)dbContext).parse(context.getUserId(), null);
			((MongoContext)dbContext).setUserId(userId);
		}
		
		UserInfo<ObjectId> userInfo = new UserInfo<ObjectId>(context.getUserInfo(), (MongoContext)dbContext);
		((MongoContext)dbContext).setUserInfo(userInfo);
		
		return (MongoContext)dbContext;
	}

	@Override
    public boolean isRoot(ObjectId id) {
	    return equals(CONTEXT_ROOT_ID, id);
    }

	@Override
    public boolean isNullOrEmpty(ObjectId id) {
	    return (id == null);
    }

	@Override
    public IUser<ObjectId> getUser() {
		UserEmbed user = new UserEmbed();
		user.setId(this.getUserId());
		
		UserInfo<ObjectId> userInfo = this.getUserInfo();
		if (userInfo != null) {
			user.setEmail(userInfo.getUsername());
			user.setName(userInfo.getFullname());
		}
		
	    return user;
    }
}
