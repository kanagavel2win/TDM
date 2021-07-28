package com.tdm.tdm.service;

import java.util.List;

import com.tdm.tdm.entity.ProfileLayoutFields;


public interface ProfileLayoutFieldsService {
	
	ProfileLayoutFields findByID(int ID);
	ProfileLayoutFields save(ProfileLayoutFields obj);
	List<ProfileLayoutFields> findAll();
	List<ProfileLayoutFields> findByProfileID(int ID);
	List<ProfileLayoutFields> findByProfileIDAndLineID(int profileID,int lineID);
	public void deleteByID(int ID);
}
