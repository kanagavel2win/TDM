package com.tdm.tdm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdm.tdm.dao.ProfileMasterRepository;
import com.tdm.tdm.entity.ProfileMaster;

@Service
public class ProfileMasterImpl implements ProfileMasterService {

	@Autowired
	ProfileMasterRepository profileMasterRepository;

	@Override
	public ProfileMaster findByProfileID(int ID) {

		Optional<ProfileMaster> result = profileMasterRepository.findById(ID);
		ProfileMaster profile = null;
		if (result.isPresent()) {
			profile = result.get();
		} else {
			throw new RuntimeException("Did not find member id - " + ID);
		}
		return profile;

	}

	@Override
	public ProfileMaster save(ProfileMaster obj) {
		// TODO Auto-generated method stub
		return profileMasterRepository.save(obj);
	}

	@Override
	public List<ProfileMaster> findAll() {
		return profileMasterRepository.findAll();
	}

}
