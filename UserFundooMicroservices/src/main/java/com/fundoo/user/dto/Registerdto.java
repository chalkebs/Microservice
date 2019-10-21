package com.fundoo.user.dto;

import javax.validation.constraints.NotEmpty;

public class Registerdto 
{
	@NotEmpty(message = "Please provide firstName")
	private String firstName;
	@NotEmpty(message = "Please provide lastName")
	private String lastName;
	@NotEmpty(message = "Please provide emailId")
	private String emailId;
	@NotEmpty(message = "Please provide password")
	private String password;
	@NotEmpty(message = "Please provide mobileNumber")
	private String mobileNum;

	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
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
	public String getMobileNum() 
	{
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) 
	{
		this.mobileNum = mobileNum;
	}
}
