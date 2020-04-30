package vn.vsd.agro.domain.embed;

import java.io.Serializable;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.UserInfo;
import vn.vsd.agro.entity.SimpleDateTime;
import vn.vsd.agro.util.DateTimeUtils;

public class StatusEmbed implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = 7904702497987267836L;
    
    public static StatusEmbed createStatusEmbed(IContext<ObjectId> context, int status) {
    	UserInfo<ObjectId> userInfo = context.getUserInfo();
    	
    	UserEmbed user = new UserEmbed(context.getUserId(), 
    			(userInfo == null ? "unknown" : userInfo.getUsername()), 
    			(userInfo == null ? "Unknown" : userInfo.getFullname()));
    	
    	return new StatusEmbed(user, status); 
    }
    
    private SimpleDateTime date;
    private UserEmbed user;
    private int status;
    private String comment;
    
    public StatusEmbed() {
    	super();
    }
    
	public StatusEmbed(SimpleDateTime date, UserEmbed user, int status) {
	    this();
	    
	    this.date = date;
	    this.user = user;
	    this.status = status;
    }

	public StatusEmbed(UserEmbed user, int status) {
		this(DateTimeUtils.getCurrentTime(), user, status);
	}
	
	public SimpleDateTime getDate() {
		return date;
	}

	public void setDate(SimpleDateTime date) {
		this.date = date;
	}

	public UserEmbed getUser() {
		return user;
	}

	public void setUser(UserEmbed user) {
		this.user = user;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
