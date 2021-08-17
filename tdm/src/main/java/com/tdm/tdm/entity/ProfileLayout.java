package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "profileLayout")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ProfileLayout implements Comparable<ProfileLayout> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int orderID;
	private int profileID;
	private String lineType;
	private String lineTitle;
	private String lineLength;

	

	@Override
	public int compareTo(ProfileLayout o) {

		if (this.getOrderID() == o.getOrderID())
			return 0;
		else if (this.getOrderID() > o.getOrderID())
			return 1;
		else
			return -1;
	}

}
