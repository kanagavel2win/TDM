package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "profileMaster")
@NoArgsConstructor @RequiredArgsConstructor 
@Getter @Setter @ToString
public class ProfileMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int profileid;
	
	@NotNull
	private String profileName;
	private String description;
		
}
