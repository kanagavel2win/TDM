package com.tdm.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="dataMaster")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataMaster implements Comparable<DataMaster>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	private int profileID;
	private int lineID;
	
	@Column(length=6000)
	private String data_Details;
	private int orderID;

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
