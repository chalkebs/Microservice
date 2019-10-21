package com.fundoo.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"creationTime", "updationTime"},allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ConfigurationProperties
public class NoteEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noteId;
	
	private String noteTitle;

	private String noteData;
	
	private boolean pinNote;
	
	private boolean archiveNote;
	
	private boolean trashNote;
	
	private LocalDateTime trashedTime;
	
	private String color;
	
	private String fileName;
	
	private LocalDateTime dateTime;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date creationTime;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
	private Date updationTime;
	
	private Long userEntityId;

	@JsonIgnore
	@ManyToMany
    private List<LabelEntity> labels;
	
	@JsonIgnore
	@ManyToMany
	private List<CollaboratorsEntity> collabs;
	
	//getterSetters
	
	public List<CollaboratorsEntity> getCollabs() 
	{
		return collabs;
	}

	public void setCollabs(List<CollaboratorsEntity> collabs) 
	{
		this.collabs = collabs;
	}

	public List<LabelEntity> getLabels() 
	{
		return labels;
	}

	public void setLabels(List<LabelEntity> labels) 
	{
		this.labels = labels;
	}
	
	public Long getNoteId() 
	{
		return noteId;
	}

	public void setNoteId(Long noteId) 
	{
		this.noteId = noteId;
	}

	public String getNoteTitle() 
	{
		return noteTitle;
	}
	
	public void setNoteTitle(String noteTitle) 
	{
		this.noteTitle = noteTitle;
	}
	
	public String getNoteData() 
	{
		return noteData;
	}

	public void setNoteData(String noteData) 
	{
		this.noteData = noteData;
	}

	public Date getCreationTime() 
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime) 
	{
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() 
	{
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) 
	{
		this.updationTime = updationTime;
	}

	public Long getUserEntityId() 
	{
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId) 
	{
		this.userEntityId = userEntityId;
	}

	public boolean isPinNote() {
		return pinNote;
	}

	public void setPinNote(boolean pinNote) {
		this.pinNote = pinNote;
	}

	public boolean isArchiveNote() {
		return archiveNote;
	}

	public void setArchiveNote(boolean archiveNote) {
		this.archiveNote = archiveNote;
	}

	public boolean isTrashNote() {
		return trashNote;
	}

	public void setTrashNote(boolean trashNote) {
		this.trashNote = trashNote;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getTrashedTime() {
		return trashedTime;
	}

	public void setTrashedTime(LocalDateTime trashedTime) {
		this.trashedTime = trashedTime;
	}

}
