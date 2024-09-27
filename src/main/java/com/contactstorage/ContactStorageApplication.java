package com.contactstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// this annotation includes - configuration, auto configuration and component scan
public class ContactStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactStorageApplication.class, args);
	}

}
