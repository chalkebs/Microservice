package com.fundoo.user.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("unused")
@Entity
@Table
@ConfigurationProperties
public class UserEntity 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private String mobileNum;
	private boolean isVerify;
	private String imageUrl;
	
	private LocalDateTime registerDate = LocalDateTime.now();
	
	/*
	 * @JsonIgnore
	 * 
	 * @OneToMany private List<NoteEntity> notes;
	 * 
	 * @JsonIgnore
	 * 
	 * @OneToMany private List<LabelEntity> labels;
	 * 
	 * @JsonIgnore
	 * 
	 * @OneToMany private List<CollaboratorsEntity> collabs;
	 * 
	 * //getterSetters
	 * 
	 * public List<CollaboratorsEntity> getCollabs() { return collabs; }
	 * 
	 * public void setCollabs(List<CollaboratorsEntity> collabs) { this.collabs =
	 * collabs; }
	 */

	public String getImageUrl() 
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) 
	{
		this.imageUrl = imageUrl;
	}

	/*
	 * public List<LabelEntity> getLabels() { return labels; }
	 * 
	 * public void setLabels(List<LabelEntity> labels) { this.labels = labels; }
	 * 
	 * public List<NoteEntity> getNotes() { return notes; }
	 * 
	 * public void setNotes(List<NoteEntity> notes) { this.notes = notes; }
	 */
	
	public Long getUserId() 
	{
		return userId;
	}
	public void setUserId(Long userId) 
	{
		this.userId = userId;
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
	public boolean isVerify() 
	{
		return isVerify;
	}
	public void setVerify(boolean isVerify)
	{
		this.isVerify = isVerify;
	}
	public LocalDateTime getRegisterDate() 
	{
		return registerDate;
	}
	public void setRegisterDate(LocalDateTime registerDate) 
	{
		this.registerDate = registerDate;
	}
	
}
