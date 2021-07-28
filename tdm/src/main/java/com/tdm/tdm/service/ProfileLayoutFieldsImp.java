package com.tdm.tdm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdm.tdm.dao.ProfileLayoutFieldsRepository;
import com.tdm.tdm.entity.ProfileLayoutFields;

@Service
public class ProfileLayoutFieldsImp implements ProfileLayoutFieldsService {

	@Autowired
	ProfileLayoutFieldsRepository ProfileLayoutFieldsRepository;

	@Override
	public ProfileLayoutFields findByID(int ID) {
		Optional<ProfileLayoutFields> result = ProfileLayoutFieldsRepository.findById(ID);
		ProfileLayoutFields profile = null;
		if (result.isPresent()) {
			profile = result.get();
		} else {
			throw new RuntimeException("Did not find member id - " + ID);
		}
		return profile;
		
	}

	@Override
	public ProfileLayoutFields save(ProfileLayoutFields obj) {
		
		return ProfileLayoutFieldsRepository.save(obj);
	}

	@Override
	public List<ProfileLayoutFields> findAll() {
		return ProfileLayoutFieldsRepository.findAll();
	}

	@Override
	public List<ProfileLayoutFields> findByProfileID(int ID) {
		return ProfileLayoutFieldsRepository.findByProfileID(ID);
	}

	@Override
	public void deleteByID(int ID) {
		 ProfileLayoutFieldsRepository.deleteById(ID);
		
	}

	@Override
	public List<ProfileLayoutFields> findByProfileIDAndLineID(int profileID, int lineID) {
		return ProfileLayoutFieldsRepository.findByProfileIDAndLineID(profileID,lineID);
	}

}
