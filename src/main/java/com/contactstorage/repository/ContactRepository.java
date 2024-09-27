package com.contactstorage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactstorage.model.Contact;
import com.contactstorage.model.User;

public interface ContactRepository extends JpaRepository<Contact,Integer>{
	
	public List<Contact> findByUser(User user);
	
	
    // search method
    public List<Contact> findByNameContainingAndUser(String name,User user);
}
