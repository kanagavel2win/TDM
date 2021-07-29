package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "profileMaster")
public class ProfileMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int profileid;
	
	@NotNull
	private String profileName;
	private String description;
	public int getProfileid() {
		return profileid;
	}
	public void setProfileid(int profileid) {
		this.profileid = profileid;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ProfileMaster() {
		
	}
	public ProfileMaster(int profileid, @NotNull String profileName, String description) {
		super();
		this.profileid = profileid;
		this.profileName = profileName;
		this.description = description;
	}
	@Override
	public String toString() {
		return "ProfileMaster [profileid=" + profileid + ", profileName=" + profileName + ", description=" + description
				+ "]";
	}

	
	
}
