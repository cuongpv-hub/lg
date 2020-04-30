package vn.vsd.agro.context;

import java.io.Serializable;

public interface IClient<K extends Serializable> extends Serializable
{
	public K getId();
	
	public String getName();
	
	public String getDomain();
	
	public String idAsString();
}
