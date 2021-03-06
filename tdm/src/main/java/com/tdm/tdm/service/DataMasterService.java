package com.tdm.tdm.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdm.tdm.dao.DataMasterRepository;
import com.tdm.tdm.entity.DataMaster;

@Service
@Transactional
public class DataMasterService implements DataMasterImp {

	@Autowired
	DataMasterRepository dataMasterRepository;

	@Override
	public DataMaster Save(DataMaster obj) {
		return dataMasterRepository.save(obj);
	}

	@Override
	public DataMaster findbyID(int id) {

		Optional<DataMaster> objList = dataMasterRepository.findById(id);

		DataMaster dataMaster = null;

		if (objList.isPresent()) {
			dataMaster = objList.get();
		} else {
			throw new RuntimeException("Data not found for " + id);
		}

		return dataMaster;
	}

	@Override
	public List<DataMaster> FindAll() {
		return dataMasterRepository.findAll();
	}

	@Override
	public void DeleteByID(int id) {
		dataMasterRepository.deleteById(id);

	}

	@Override
	public List<DataMaster> FindbyProfileID(int id) {
		return dataMasterRepository.findByProfileID(id);
	}

	@Override
	public List<DataMaster> SaveAll(List dataMasterList) {
		return dataMasterRepository.saveAll(dataMasterList);
	}

	@Override
	public void DeleteByProfileID(int id) {
		dataMasterRepository.deleteByProfileID(id);
		
	}

	@Override
	public List<DataMaster> findByProfileIDOrderbyorderID(int profileid) {
		return dataMasterRepository.findByProfileIDOrderbyorderID(profileid);
	}

	@Override
	public void deleteByProfileIDAndLineID(int profileid, int lineid) {
		
		dataMasterRepository.deleteByProfileIDAndLineID(profileid, lineid);
	}

}
