package vn.vsd.agro.dto.embed;

import vn.vsd.agro.dto.DTOEmbed;

public class UserEmbedDto extends DTOEmbed<String> {
	/**
	 * 
	 */
    private static final long serialVersionUID = 5689092223088750722L;
    
    private String email;
	private String name;
	
	public UserEmbedDto() {
	    super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
