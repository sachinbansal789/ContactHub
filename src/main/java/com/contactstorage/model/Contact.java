package com.contactstorage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "contacts")
public class Contact {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "contact_id")
	private Integer cid;
	
	
	@NotBlank(message="value required")
	@Size(min=3,max=15,message="number of characters should be in range")
	private String name;
	
	private String nickname;
	
	@NotBlank
	private String work;
	
	@NotBlank
	@Column(length=10)
	private String phone;
	
//	@Override
//	public String toString() {
//		return "Contact [cid=" + cid + ", name=" + name + ", nickname=" + nickname + ", work=" + work + ", phone="
//				+ phone + ", email=" + email + ", image=" + image + ", description=" + description + ", user=" + user
//				+ "]";
//	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Email
	@NotBlank
	@Column( nullable= false , unique = true)
	private String email;
	
	
	private String image;
	
	
	@Column(length =1000)
	private String description;
	
	@ManyToOne
	// jsonIgnore - this annotation stops user from getting serialized
	@JsonIgnore
//JoinColumn(name = "user_id", referencedColumnName= "user_id")
	private User user;

	// we have to overrirde equals method becoz we hav to match contact object which is there vs with which we hav to match
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid==((Contact) obj).getCid();
   
	
	}
}
