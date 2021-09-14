package com.tdm.tdm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tdm.tdm.entity.DataMaster;

@Repository
public interface DataMasterRepository extends JpaRepository<DataMaster, Integer> {

	List<DataMaster> findByProfileID(int id);

	void deleteByProfileID(int id);
	
	@Query("select dm from DataMaster dm where dm.profileID=:profileID order by orderID")
	List<DataMaster> findByProfileIDOrderbyorderID(@Param("profileID") int id);
	
	void deleteByProfileIDAndLineID(int profileid,int lineid);
}
