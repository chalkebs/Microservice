package com.fundoo.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseOfToken 
{
	private String statusMessage;	
	private int statusCode;
	private String token;
	
	public String getToken() 
	{
		return token;
	}

	public void setToken(String token) 
	{
		this.token = token;
	}

	public String getStatusMessage() 
	{
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) 
	{
		this.statusMessage = statusMessage;
	}
	
	public int getStatusCode() 
	{
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) 
	{
		this.statusCode = statusCode;
	}
	
	
	@Override
	public String toString() {
		return "ResponseOfToken [statusMessage=" + statusMessage + ", "
				+ "statusCode=" + statusCode + ", token=" + token
				+ "]";
	}
	
}
