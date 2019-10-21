package com.fundoo.user.dto;

import javax.persistence.Column;

import javax.validation.constraints.NotEmpty;

public class Logindto 
{
	@Column(name = "email", nullable = false)
	@NotEmpty(message = "Please provide valid Email ID")
	private String emailId;

	@NotEmpty(message = "Please provide password")
	@Column(name = "password")
	private String password;

	public String getEmailId() 
	{
		return emailId;
	}

	public void setEmailId(String emailId) 
	{
		this.emailId = emailId;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	
}
