package com.tdm.tdm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdm.tdm.entity.DataMaster;

@Repository
public interface DataMasterRepository extends JpaRepository<DataMaster, Integer> {

	List<DataMaster> findByProfileID(int id);

	void deleteByProfileID(int id);
}
