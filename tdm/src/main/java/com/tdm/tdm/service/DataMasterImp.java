package com.tdm.tdm.service;

import java.util.List;

import com.tdm.tdm.entity.DataMaster;

public interface DataMasterImp {

	public DataMaster Save(DataMaster obj);

	DataMaster findbyID(int id);

	List<DataMaster> FindAll();

	List<DataMaster> FindbyProfileID(int id);

	void DeleteByID(int id);

	List<DataMaster> SaveAll(List dataMasterList);

	void DeleteByProfileID(int id);
	
	List<DataMaster> findByProfileIDOrderbyorderID(int profileid);
	
	void deleteByProfileIDAndLineID(int profileid,int lineid);
}
