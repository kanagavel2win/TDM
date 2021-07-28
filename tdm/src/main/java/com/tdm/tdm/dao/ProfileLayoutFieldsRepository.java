package com.tdm.tdm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdm.tdm.entity.ProfileLayoutFields;

@Repository
public interface ProfileLayoutFieldsRepository extends JpaRepository<ProfileLayoutFields, Integer> {

	List<ProfileLayoutFields> findByProfileID(int ID);

	List<ProfileLayoutFields> findByProfileIDAndLineID(int profileID, int lineID);

}
