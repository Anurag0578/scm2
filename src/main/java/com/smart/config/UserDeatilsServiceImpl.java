package com.smart.config;
import org.springframework.beans.factory.annotation.Autowired;

import  org.springframework.security.core.userdetails.UserDetails;
import  org.springframework.security.core.userdetails.UserDetailsService;
import  org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart.entities.User;
import com.smart.dao.UserRepositiory;
@Service
public class UserDeatilsServiceImpl implements UserDetailsService{

	@Autowired 
	private UserRepositiory userRepositiory;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
	
		
		//fetching user from database
		User user =userRepositiory.getUserByUserName(username);
		
		if(user == null)
		{
			throw new UsernameNotFoundException("Could Not Found User !!");
		}
		
			//CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return new CustomUserDetails(user);
	
	}
}
