package com.contactstorage.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contactstorage.model.User;
import com.contactstorage.repository.UserRepository;


public class CustomUserDetailsService implements UserDetailsService {
	
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
	//	Optional<User> user = userRepository.findUserByEmail(email);
	
		
		
	//	Optional<CustomUserDetails> customUserDetails =  new CustomUserDetails(user).get();
		
		User user = userRepository.findUserByEmail(email);
		
		if(user==null) {
			
			throw new UsernameNotFoundException("user to nahi mila");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
