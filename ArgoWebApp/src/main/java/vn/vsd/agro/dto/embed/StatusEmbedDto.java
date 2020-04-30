package vn.vsd.agro.dto.embed;

import java.io.Serializable;

public class StatusEmbedDto implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = -7373281604755605338L;
    
    public static final StatusEmbedDto defaulStatus() {
    	StatusEmbedDto status = new StatusEmbedDto();
    	status.setStatus(0);
    	
    	return status;
    }
    
	private String date;
    private UserEmbedDto user;
    private int status;
    private String comment;
    
	public StatusEmbedDto() {
		super();
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserEmbedDto getUser() {
		return user;
	}

	public void setUser(UserEmbedDto user) {
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
