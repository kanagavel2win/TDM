package com.tdm.tdm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdm.tdm.dao.ProfileLayoutRepository;
import com.tdm.tdm.entity.ProfileLayout;

@Service
public class ProfileLayoutImp implements ProfileLayoutService {

	@Autowired
	ProfileLayoutRepository profileLayoutRepository;

	@Override
	public ProfileLayout findByID(int ID) {
		Optional<ProfileLayout> result = profileLayoutRepository.findById(ID);
		ProfileLayout profile = null;
		if (result.isPresent()) {
			profile = result.get();
		} else {
			throw new RuntimeException("Did not find member id - " + ID);
		}
		return profile;
		
	}

	@Override
	public ProfileLayout save(ProfileLayout obj) {
		
		return profileLayoutRepository.save(obj);
	}

	@Override
	public List<ProfileLayout> findAll() {
		return profileLayoutRepository.findAll();
	}

	@Override
	public List<ProfileLayout> findByProfileID(int ID) {
		return profileLayoutRepository.findByProfileID(ID);
	}

	@Override
	public void deleteByID(int ID) {
		 profileLayoutRepository.deleteById(ID);
		
	}

}
