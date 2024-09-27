package com.contactstorage.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactstorage.model.Contact;
import com.contactstorage.model.User;
import com.contactstorage.repository.ContactRepository;
import com.contactstorage.repository.UserRepository;

@RestController
public class SearchController{
	
	@Autowired 
	ContactRepository contactRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> Search(@PathVariable("query")String query,Principal principal){
		
		User user = userRepository.findUserByEmail(principal.getName());
		
		List<Contact> contact=contactRepository.findByNameContainingAndUser(query, user);
		 
		return ResponseEntity.ok(contact);
		
	}
	
	
	
}