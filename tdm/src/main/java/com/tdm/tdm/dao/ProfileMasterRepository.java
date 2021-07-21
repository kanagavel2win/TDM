package com.tdm.tdm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdm.tdm.entity.ProfileMaster;

@Repository
public interface ProfileMasterRepository extends JpaRepository<ProfileMaster, Integer> {

}
