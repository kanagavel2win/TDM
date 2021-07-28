package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "profileLayout")
public class ProfileLayout implements Comparable<ProfileLayout> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int orderID;
	private int profileID;
	private String lineType;
	private String lineTitle;
	private String lineLength;

	public ProfileLayout() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getProfileID() {
		return profileID;
	}

	public void setProfileID(int profileID) {
		this.profileID = profileID;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getLineTitle() {
		return lineTitle;
	}

	public void setLineTitle(String lineTitle) {
		this.lineTitle = lineTitle;
	}

	public String getLineLength() {
		return lineLength;
	}

	public void setLineLength(String lineLength) {
		this.lineLength = lineLength;
	}

	public ProfileLayout(int id, int orderID, int profileID, String lineType, String lineTitle, String lineLength) {
		super();
		this.id = id;
		this.orderID = orderID;
		this.profileID = profileID;
		this.lineType = lineType;
		this.lineTitle = lineTitle;
		this.lineLength = lineLength;
	}

	@Override
	public String toString() {
		return "ProfileLayout [id=" + id + ", orderID=" + orderID + ", profileID=" + profileID + ", lineType="
				+ lineType + ", lineTitle=" + lineTitle + ", lineLength=" + lineLength + "]";
	}

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
