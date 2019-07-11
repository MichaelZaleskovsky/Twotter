package com.twotter.models;

import com.twotter.entities.PostEntity;

import io.swagger.annotations.ApiModelProperty;

public class PostResponse {
	
    @ApiModelProperty(notes = "Unic posts ID",name="id",required=true,value="12345")
	private long id;
	
    @ApiModelProperty(notes = "Tim when post was created in ms",name="time",required=true,value="12345")
	private long time;
	
    @ApiModelProperty(notes = "User's name",required=true, value="User")
	private String username;
	
    @ApiModelProperty(notes = "Text of the post",required=true, value="Text")
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
