package vn.vsd.agro.context;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IContext<T extends Serializable> extends Serializable {
	public static final String ROOT_CLIENT_NAME = "VSD";
	
    public T getRootId();
    public T newId();
    public T getModified();
    
	public T getClientId();
	public String getClientName();
	
	public T getOrgId();
	public T getUserId();
	
	public UserInfo<T> getUserInfo();
	
	public IUser<T> getUser();
	
	public boolean isRoot(T id);
	public boolean isNullOrEmpty(T id);
	
	public boolean hasRole(String...roles);
	
	public T parse(Object value, T defaultValue);
	
	public List<T> parseMulti(Collection<?> values, boolean ignoreEmptyValue);
	
	public boolean equals(T id1, T id2);
	
	public boolean in(T id, Collection<T> values);
	
	public IContext<T> getRootContext();
	
	public IContext<?> getDbContext(IContext<String> loginContext, IClient<?> client);
}
