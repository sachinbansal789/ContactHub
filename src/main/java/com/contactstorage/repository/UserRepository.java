package com.contactstorage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactstorage.model.User;

public interface UserRepository extends JpaRepository<User ,Integer> {
	
 	User findUserByEmail(String email);
	
//	Optional<User> findUserByEmail(String email);
	
//	User findUserByEmail(String email);
	
 //   public User findUserByUserName(String email);

}
