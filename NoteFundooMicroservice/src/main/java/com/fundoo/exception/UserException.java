package com.fundoo.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus
//TODO update extent to exception 
public class UserException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String statusMsg;
	
	public UserException(String statusMsg)
	{
		super(statusMsg);
	}
	
	public UserException(int code, String statusMsg)
	{
		super(statusMsg);
		this.code =code;
	}
}
