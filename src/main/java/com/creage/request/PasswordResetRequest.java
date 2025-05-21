package com.creage.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordResetRequest {
	
    @JsonProperty("oldPassword")
	private String oldPassword;
    private String email;
    private String password;
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
	public String getOldPass() {
		return oldPassword;
	}
	public void setOldPass(String oldPass) {
		this.oldPassword = oldPass;
	}
    
    
}

