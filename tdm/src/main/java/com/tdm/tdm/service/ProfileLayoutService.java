package com.tdm.tdm.service;

import java.util.List;

import com.tdm.tdm.entity.ProfileLayout;


public interface ProfileLayoutService {
	
	ProfileLayout findByID(int ID);
	ProfileLayout save(ProfileLayout obj);
	List<ProfileLayout> findAll();
	List<ProfileLayout> findByProfileID(int ID);
}
