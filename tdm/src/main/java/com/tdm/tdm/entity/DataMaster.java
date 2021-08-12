package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dataMaster")
public class DataMaster implements Comparable<DataMaster>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private int profileID;
	private int lineID;
	
	private String data_Details;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getProfileID() {
		return profileID;
	}

	public void setProfileID(int profileID) {
		this.profileID = profileID;
	}

	public int getLineID() {
		return lineID;
	}

	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	public String getData_Details() {
		return data_Details;
	}

	public void setData_Details(String data_Details) {
		this.data_Details = data_Details;
	}

	@Override
	public String toString() {
		return "DataMaster [ID=" + ID + ", profileID=" + profileID + ", lineID=" + lineID + ", data_Details="
				+ data_Details + "]";
	}

	public DataMaster(int iD, int profileID, int lineID, String data_Details) {
		super();
		ID = iD;
		this.profileID = profileID;
		this.lineID = lineID;
		this.data_Details = data_Details;
	}
	
	public DataMaster() {
				
	}

	@Override
	public int compareTo(DataMaster o) {
		if(this.lineID == o.lineID)
		{
			return 0;
		}else if (this.lineID > o.lineID)
		{
			return 1;
		}else
		{
			return -1;	
		}
		
	}
}
