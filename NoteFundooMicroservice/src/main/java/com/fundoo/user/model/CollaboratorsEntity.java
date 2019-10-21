package com.fundoo.user.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@ConfigurationProperties
public class CollaboratorsEntity 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long collaboratorsId;
	
	@NotBlank
	private String collaboratorsName;
	
	private Long noteId;
	
	private Long userEntityId;
	
	@JsonIgnore
	@ManyToMany
	private List<NoteEntity> notes;
	
	//getterSetters

	public List<NoteEntity> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteEntity> notes) {
		this.notes = notes;
	}

	public Long getCollaboratorsId() 
	{
		return collaboratorsId;
	}
	
	public void setCollaboratorsId(Long collaboratorsId) 
	{
		this.collaboratorsId = collaboratorsId;
	}
	
	public String getCollaboratorsName() 
	{
		return collaboratorsName;
	}
	
	public void setCollaboratorsName(String collaboratorsName) 
	{
		this.collaboratorsName = collaboratorsName;
	}

	public Long getNoteId() 
	{
		return noteId;
	}

	public void setNoteId(Long noteId) 
	{
		this.noteId = noteId;
	}

	public Long getUserEntityId() {
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId) {
		this.userEntityId = userEntityId;
	}
	
}
