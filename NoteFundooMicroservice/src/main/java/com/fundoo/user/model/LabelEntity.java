package com.fundoo.user.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class LabelEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long noteId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long labelId;
	
	@NotBlank
	private String labelTitle;
	
	private String color;
	
	private Long userEntityId;
	
	@JsonIgnore
	@ManyToMany
    private List<NoteEntity> notes;

	//getterSetters
	
	public List<NoteEntity> getNotes() 
	{
		return notes;
	}

	public void setNotes(List<NoteEntity> notes) 
	{
		this.notes = notes;
	}
	
	public Long getNoteId() 
	{
		return noteId;
	}

	public void setNoteId(Long noteId) 
	{
		this.noteId = noteId;
	}

	public Long getLabelId() 
	{
		return labelId;
	}

	public void setLabelId(Long labelId) 
	{
		this.labelId = labelId;
	}

	public String getLabelTitle() 
	{
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) 
	{
		this.labelTitle = labelTitle;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getUserEntityId() {
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId) {
		this.userEntityId = userEntityId;
	}

}
