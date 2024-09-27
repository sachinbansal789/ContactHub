package com.contactstorage.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.contactstorage.model.User;

public class CustomUserDetails implements UserDetails {
	
	
	// in dono steps se hum user ko use kr skte h customuserdetails mein 
	private User user;
	
	CustomUserDetails(User user) {
		super();
		
		this.user=user;
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
         
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());

		return List.of(simpleGrantedAuthority);
		
		
//	List<GrantedAuthority>  authorityList =  new ArrayList<>();
//		super.getRoles().forEach(role -> {
//		authorityList.add(new SimpleGrantedAuthority(role.getName()));
//	});
//		return authorityList;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
