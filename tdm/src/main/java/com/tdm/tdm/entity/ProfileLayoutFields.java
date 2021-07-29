package com.tdm.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PROFILELAYOUTFIELDS")
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

	public ProfileLayoutFields() {

	}

	public ProfileLayoutFields(int iD, @NotNull int profileID, @NotNull int lineID, int cOLUMN_NO, String fIELDNAME,
			String sTART_POS, String eND_POS, String f_DATATYPE, String dEFAULT_VALUE, String f_MIN, String f_MAX,
			String gENERATE_TYPE, String cUSTOM_DATA_FORMAT) {
		super();
		ID = iD;
		this.profileID = profileID;
		this.lineID = lineID;
		COLUMN_NO = cOLUMN_NO;
		FIELDNAME = fIELDNAME;
		START_POS = sTART_POS;
		END_POS = eND_POS;
		F_DATATYPE = f_DATATYPE;
		DEFAULT_VALUE = dEFAULT_VALUE;
		F_MIN = f_MIN;
		F_MAX = f_MAX;
		GENERATE_TYPE = gENERATE_TYPE;
		CUSTOM_DATA_FORMAT = cUSTOM_DATA_FORMAT;
	}

	@Override
	public String toString() {
		return "ProfileLayoutFields [ID=" + ID + ", profileID=" + profileID + ", lineID=" + lineID + ", COLUMN_NO="
				+ COLUMN_NO + ", FIELDNAME=" + FIELDNAME + ", START_POS=" + START_POS + ", END_POS=" + END_POS
				+ ", F_DATATYPE=" + F_DATATYPE + ", DEFAULT_VALUE=" + DEFAULT_VALUE + ", F_MIN=" + F_MIN + ", F_MAX="
				+ F_MAX + ", GENERATE_TYPE=" + GENERATE_TYPE + ", CUSTOM_DATA_FORMAT=" + CUSTOM_DATA_FORMAT + "]";
	}

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

	public int getCOLUMN_NO() {
		return COLUMN_NO;
	}

	public void setCOLUMN_NO(int cOLUMN_NO) {
		COLUMN_NO = cOLUMN_NO;
	}

	public String getFIELDNAME() {
		return FIELDNAME;
	}

	public void setFIELDNAME(String fIELDNAME) {
		FIELDNAME = fIELDNAME;
	}

	public String getSTART_POS() {
		return START_POS;
	}

	public void setSTART_POS(String sTART_POS) {
		START_POS = sTART_POS;
	}

	public String getEND_POS() {
		return END_POS;
	}

	public void setEND_POS(String eND_POS) {
		END_POS = eND_POS;
	}

	public String getF_DATATYPE() {
		return F_DATATYPE;
	}

	public void setF_DATATYPE(String f_DATATYPE) {
		F_DATATYPE = f_DATATYPE;
	}

	public String getDEFAULT_VALUE() {
		return DEFAULT_VALUE;
	}

	public void setDEFAULT_VALUE(String dEFAULT_VALUE) {
		DEFAULT_VALUE = dEFAULT_VALUE;
	}

	public String getF_MIN() {
		return F_MIN;
	}

	public void setF_MIN(String f_MIN) {
		F_MIN = f_MIN;
	}

	public String getF_MAX() {
		return F_MAX;
	}

	public void setF_MAX(String f_MAX) {
		F_MAX = f_MAX;
	}

	public String getGENERATE_TYPE() {
		return GENERATE_TYPE;
	}

	public void setGENERATE_TYPE(String gENERATE_TYPE) {
		GENERATE_TYPE = gENERATE_TYPE;
	}

	public String getCUSTOM_DATA_FORMAT() {
		return CUSTOM_DATA_FORMAT;
	}

	public void setCUSTOM_DATA_FORMAT(String cUSTOM_DATA_FORMAT) {
		CUSTOM_DATA_FORMAT = cUSTOM_DATA_FORMAT;
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
