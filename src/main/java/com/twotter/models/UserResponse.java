package com.twotter.models;

import com.twotter.entities.UserEntity;

public class UserResponse {
	
	private String userId;
	private String username;

	public UserResponse(UserEntity usr) {
		this.userId = usr.getUserId();
		this.username = usr.getUsername();
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
