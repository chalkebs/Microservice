package com.fundoo.user.dto;

import javax.validation.constraints.NotBlank;

public class LabelDto 
{
	@NotBlank
	private String labelTitle;

	public String getLabelTitle() 
	{
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) 
	{
		this.labelTitle = labelTitle;
	}
}
