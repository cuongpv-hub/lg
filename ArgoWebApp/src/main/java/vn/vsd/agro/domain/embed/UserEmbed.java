package vn.vsd.agro.domain.embed;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IUser;
import vn.vsd.agro.domain.POEmbed;
import vn.vsd.agro.domain.User;

public class UserEmbed extends POEmbed<ObjectId> implements IUser<ObjectId> {

    private static final long serialVersionUID = 6422701600145590857L;

    private String name;
    private String email;
    
    public UserEmbed() {
        super();
    }

    public UserEmbed(User user) {
        super(user);
        
        if (user != null) {
	        this.name = user.getName();
	        this.email = user.getEmail();
        }
    }
    
    public UserEmbed(UserEmbed user) {
        super(user);
        
        if (user != null) {
	        this.email = user.getEmail();
	        this.name = user.getName();
        }
    }
    
    public UserEmbed(ObjectId id, String email, String name) {
        super();
        
        setId(id);
        this.email = email;
        this.name = name;
    }

    @Override
    public String getUsername() {
        return email;
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
}
