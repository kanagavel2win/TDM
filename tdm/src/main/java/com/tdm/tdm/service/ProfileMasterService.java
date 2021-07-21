package com.tdm.tdm.service;

import java.util.List;

import com.tdm.tdm.entity.ProfileMaster;


public interface ProfileMasterService {
	
	ProfileMaster findByProfileID(int ID);
	ProfileMaster save(ProfileMaster obj);
	List<ProfileMaster> findAll();
	
}
