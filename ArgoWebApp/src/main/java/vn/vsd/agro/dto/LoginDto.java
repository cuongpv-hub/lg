package vn.vsd.agro.dto;

import java.io.Serializable;

public class LoginDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3968655332557995103L;

	private String account;
	private String password;
	private boolean remember;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

}
