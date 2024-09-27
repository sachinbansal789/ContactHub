package com.contactstorage.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity

 @Table( name = "USER")
public class User {
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	
	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", Name=" + Name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageurl=" + imageurl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}





	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer id;
	
	
	@NotBlank(message="value required")
	@Size(min=3,max=15,message="number of characters should be in range")
	private String Name;
	
	
	@Email
	@NotBlank
	@Column( nullable= false , unique = true)
	private String email;
	
	
	
	
	private String password;
	
	private String role;
	
	private boolean enabled;
	
	private String imageurl;
	
	
	@Column(length =500)
	private String about;
    
	// if you don't want top create an additonal table ,we have to use mapped bytherwise it will creaate a table naming User_contact
	
	// orphan removal=true will delete the contact ,as soon as you unlink it with user, while deleting contact we 
	// are unlinking it with user,so its not displaying but not getting deleted but when we use orphanRemoval=true
	// contact will get deleted automatically as soon as you unlink it with user
	
	@OneToMany(cascade=CascadeType.ALL, fetch= FetchType.LAZY , mappedBy="user",orphanRemoval=true	)
//	@JoinColumn(Name="contact_id" , referencedColumnName ="contact_id")
    private List<Contact> contacts;


	


	

	
	

}
