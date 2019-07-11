package com.twotter.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss.SSS")
	private Date timeStamp;
	private int errorCode;
	private String message;

	public ErrorResponse() {
	}

	public ErrorResponse(int errorCode, String message) {
		this.timeStamp = new Date();
		this.errorCode = errorCode;
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
