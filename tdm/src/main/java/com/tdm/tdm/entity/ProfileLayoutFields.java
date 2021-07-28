package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "PROFILELAYOUTFIELDS")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class ProfileLayoutFields implements Comparable<ProfileLayoutFields> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	@NotNull
	private int profileID;
	@NotNull
	private int lineID;
	
	private int COLUMN_NO;
	private String FIELDNAME;
	private String START_POS;
	private String END_POS;
	private String F_DATATYPE;
	private String DEFAULT_VALUE;
	private String F_MIN;
	private String F_MAX;
	private String GENERATE_TYPE;
	private String CUSTOM_DATA_FORMAT;

	public int getCOLUMN_NO() {
		return lineID;
	}

	@Override
	public int compareTo(ProfileLayoutFields o) {
		if (this.getCOLUMN_NO() == o.getCOLUMN_NO())
			return 0;
		else if (this.getCOLUMN_NO() > o.getCOLUMN_NO())
			return 1;
		else
			return -1;

	}

}
