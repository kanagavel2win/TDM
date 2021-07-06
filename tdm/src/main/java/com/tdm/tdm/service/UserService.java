package com.tdm.tdm.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tdm.tdm.entity.User;
import com.tdm.tdm.entity.UserRegistrationDto;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);
    User findByMemberID(String memberID);

    User save(UserRegistrationDto registration);
	UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
