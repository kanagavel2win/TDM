package com.tdm.tdm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdm.tdm.entity.ProfileLayout;

@Repository
public interface ProfileLayoutRepository extends JpaRepository<ProfileLayout, Integer> {

	List<ProfileLayout> findByProfileID(int ID);

}
