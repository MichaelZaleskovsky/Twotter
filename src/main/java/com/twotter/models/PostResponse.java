package com.twotter.models;

import com.twotter.entities.PostEntity;

import io.swagger.annotations.ApiModelProperty;

public class PostResponse {
	
	private long id;
	
	private long time;
	
	private String username;
	
	private String text;

	public PostResponse(PostEntity post) {
		this.id = post.getId();
		this.time = post.getTime();
		this.username = post.getUser().getUsername();
		this.text = post.getText();
	}
	
	public PostResponse() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
