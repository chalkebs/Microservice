package com.fundoo.user.dto;

public class ProfileDto 
{
	private String emailId;
	private String firstName;
	private String lastName;
	private String imgUrl;
	
	public String getEmailId() 
	{
		return emailId;
	}
	
	public void setEmailId(String emailId) 
	{
		this.emailId = emailId;
	}

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

	public String getImgUrl() 
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) 
	{
		this.imgUrl = imgUrl;
	}
}
