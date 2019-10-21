package com.fundoo.user.dto;

import javax.validation.constraints.NotBlank;

public class CollaboratorDto 
{
	@NotBlank
	private String collaboratorsName;

	public String getCollaboratorsName() 
	{
		return collaboratorsName;
	}

	public void setCollaboratorsName(String collaboratorsName) 
	{
		this.collaboratorsName = collaboratorsName;
	}
	
}
