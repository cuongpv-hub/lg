package vn.vsd.agro.domain;

import org.bson.types.ObjectId;

public abstract class UserExtension extends POSearchable<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5390317143445370028L;

	//public static final String COLUMNNAME_USER_ID = "userId";

	//private ObjectId userId;
	
	private volatile String[] searchUsers;

	/*
	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	*/
	
	public ObjectId getUserId() {
		return this.getId();
	}
	
	public void setUserId(ObjectId userId) {
		this.setId(userId);
	}
	
	public String[] getSearchUsers() {
		return searchUsers;
	}

	public void setSearchUsers(String[] searchUsers) {
		this.searchUsers = searchUsers;
	}

}
