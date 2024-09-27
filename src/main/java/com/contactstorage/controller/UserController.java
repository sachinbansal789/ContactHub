package com.contactstorage.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.contactstorage.Helper.Message;
import com.contactstorage.model.Contact;
import com.contactstorage.model.User;
import com.contactstorage.repository.ContactRepository;
import com.contactstorage.repository.UserRepository;
import com.razorpay.*;
  
@Controller
@RequestMapping("/user")
public class UserController {
	
	public static String uploadDir= System.getProperty("user.dir")+ "/src/main/resources/static/productImages";
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	

	// this method is added in all the handlers , user object will be added in both the handlers
    // once we login principal is used to get name and password of that logined user
	@ModelAttribute
	public void addCommonData(Model model , Principal principal , Authentication authentication) {
		
		    String email =principal.getName();
		
			User user = userRepository.findUserByEmail(email);
			
			if(user != null) {
				
				model.addAttribute("user", user);	
			}
			else
			{
				OAuth2AuthenticationToken token =(OAuth2AuthenticationToken) authentication;
				
				String Email = token.getPrincipal().getAttributes().get("email").toString();
				
				User user1 = userRepository.findUserByEmail(Email);
				
				model.addAttribute("user", user1);	
			}		
	}
	
	@RequestMapping("/index")
	public String dashboard() {
		return "dashboard";		
	}
	
	
	
	@RequestMapping("/add_Contacts")
	public String addcontact(Model model ) {
		model.addAttribute("Contact", new Contact());
		return "addContact";
	}

	
	@PostMapping("/process-contact")
	public String addContact(@Valid @ModelAttribute("Contact")Contact contact,BindingResult result,
			@RequestParam(value="profileImage")MultipartFile file,
			@RequestParam(value="agreement", defaultValue="false")boolean agreement,
			Model model, Principal principal ,HttpSession session) {
		
		try {
//			
            String email = principal.getName();
			
			User user= userRepository.findUserByEmail(email);
			
			if(!agreement) {
				throw new Exception("agrrement is not checked");
			}
			
			if(result.hasErrors()) {
				
				throw new Exception("Fill the details correctly");
			}
			
			if(file.isEmpty() ) {
				
				contact.setImage("contact.png");
				
			}
			else {
			
		// processing and adding image
		
	    //update the file to folder and set name to file in image
			
			Path fileNameAndPath =Paths.get(uploadDir , file.getOriginalFilename());
			
			Files.write(fileNameAndPath, file.getBytes());
			
		    contact.setImage(file.getOriginalFilename());
			
		// OR	
			
		// processing and adding image
			
//			contact.setImage(file.getOriginalFilename());
//			
//		           //how to find path
//			File savefile =new ClassPathResource("static/img").getFile();
//			
//			Path path =Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//			
//			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//			
			}
		
			contact.setUser(user);
			
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			//
			
			session.setAttribute("message", new Message("Contact added succefully !! ","alert-success"));
			
		}
		catch(Exception e) {
			
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!! "+e.getMessage(),"alert-danger"));
		}
		
		return "addContact";
	}
	
	@GetMapping("/show-contacts")
	public String viewContacts(Model model,Principal principal) {
		
		String email = principal.getName();
		
		User user = userRepository.findUserByEmail(email);
		
		Integer user_id = user.getId();
		
	//	List<Contact> contacts =user.getContacts();
		
//		Optional<Contact> contacts =  contactRepository.findById(user_id);
		
//		List<Contact> contacts = new ArrayList<Contact>();
				
//		contacts.add(contactRepository.findById(user_id).get());
		
	//	System.out.println(contacts);
		
	//    Optional<Contact> contacts = contactRepository.findContactsByUser(user_id);
		
		List<Contact> contact =contactRepository.findByUser(user);
		
		model.addAttribute("contact", contact);
		
		return "show_contacts";
	}
	
	// showing specifoc contact details
	
	@GetMapping("/{cid}/contact/")
	public String viewContact(@PathVariable("cid")Integer cid , Model model,Principal principal ) {
		
		String email=principal.getName();
		
		User user =userRepository.findUserByEmail(email);
		
		
		Contact contact = contactRepository.findById(cid).get();
		
		if(user.getId()==contact.getUser().getId()) {
			
			model.addAttribute("contact", contact);
		}


		return "contact_detail";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid")Integer cid,Model model , Principal principal,HttpSession session) {
		
		Contact contact=contactRepository.findById(cid).get();
		
		// how to unlink user
//		
//		contact.setUser(null);
//		
//		contactRepository.delete(contact);
//		
		
		User user=userRepository.findUserByEmail(principal.getName());
		
		user.getContacts().remove(contact);
		
		userRepository.save(user);
		
		session.setAttribute("message", new Message("contact deleted successfully!!" ,"alert-success"));
		
		return "redirect:/user/show-contacts/";
	}
	 // updating contact, opening for prewritten values
	@PostMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid")Integer cid,Model model,HttpSession session) {
		
		
		
		Contact contact=contactRepository.findById(cid).get();
	
		model.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	// saving form after writing values,and adding new image 
	@RequestMapping(value="/process-update" ,  method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute("Contact")Contact contact,@RequestParam("profileImage") MultipartFile file,
			Model model,Principal principal ,BindingResult result,HttpSession session,
			@RequestParam(value="agreement" , defaultValue="false")boolean agreement) {
		
		System.out.println("contact name"+contact.getName());
		System.out.println("contact cid"+contact.getCid());
		
		
		try {
			
			Contact oldContact=contactRepository.findById(contact.getCid()).get();
			
			if(file.isEmpty()) {
				
				 contact.setImage(oldContact.getImage());
			}
			else {
				
                Path fileNameandPath =Paths.get(uploadDir, file.getOriginalFilename());
				
				Files.write(fileNameandPath ,file.getBytes());
				
				contact.setImage(file.getOriginalFilename());
			}
						
			    User user=userRepository.findUserByEmail(principal.getName());
			    
				contact.setUser(user);
				
				contactRepository.save(contact);
				 
				session.setAttribute("message", new Message("contact added successfully" , "alert-success"));
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
			session.setAttribute("message", new Message("something went wrong"+e.getMessage(),"alert-danger"));
			
		}
		
		return "redirect:/user/"+contact.getCid()+"/contact/";
	}
	
	// your profile handler
	@RequestMapping(value="/profile" ,method=RequestMethod.GET)
	public String profilehandler(Model model,Principal principal) {
		return "normal/profile";
	}
	
	// open settings handler
	
	@GetMapping("/Settings")
	public String settings() {
		
		return "normal/settings";
	}
	
    // change password handler
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("oldpassword")String oldpassword,@RequestParam("newpassword")String newpassword
			,Principal principal,HttpSession session ) throws Exception {
        
		String email=principal.getName();
		
		User user =userRepository.findUserByEmail(email);
		
		String oldpass=user.getPassword();
		
//		if(oldpass==oldpassword) {

//         Or
		if(this.bCryptPasswordEncoder.matches(oldpassword, oldpass)){
		
		
			user.setPassword(bCryptPasswordEncoder.encode(newpassword));
			userRepository.save(user);
			session.setAttribute("message", new Message("password changed successfully","alert-success"));
		}
		else {
			session.setAttribute("message", new Message("old password does not match","alert-danger"));
			
			return "redirect:/user/Settings";
			
		}
		
		return "redirect:/user/index";
	}
	
	// creating order for payment
	
	@PostMapping("/create_order")
	@ResponseBody()
	public String createOrder(@RequestBody Map<String,Object> data) throws Exception {
		
		int amt =Integer.parseInt(data.get("amount").toString());
		
	//	RazorpayClient client = new RazorpayClient("rzp_test_LVyNV4y0TFmjRQ", "ZHRlONGiIWqON4qmmwmrJue5");
		
	//	OR from java 11
		
		var client = new RazorpayClient("rzp_test_LVyNV4y0TFmjRQ", "ZHRlONGiIWqON4qmmwmrJue5");
		
		// create a json object
		
		JSONObject obj= new JSONObject();
		
		obj.put("amount", amt*100);
		obj.put("currency","INR");
		obj.put("receipt","txn_235425");
		
		// Creating new order using jsonobject obj and razorpay api
		
		Order order=client.orders.create(obj);
		
		System.out.println(order);
		
		return order.toString();
}
}

