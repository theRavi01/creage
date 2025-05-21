package com.creage.request;


import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;




public class UserCredentials implements Serializable {
	
	private static final long serialVersionUID = 6177306667832267861L;
	
//	@NotNull(message = "Email is required")
//	@Email(message = "Invalid email format")
	private String email;
	private String password;
	private String username;
	public UserCredentials() {
		super();

	}
	
	
	public UserCredentials(String email, String password, String ownerName) {
		super();
		this.email = email;
		this.password = password;
	}




	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "UserCredentials [email=" + email + ", password=" + password + ", getUserName()=" + getEmail()
				+ ", getPassword()=" + getPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	
	

}
