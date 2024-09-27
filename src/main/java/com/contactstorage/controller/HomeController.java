package com.contactstorage.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactstorage.Helper.Message;
import com.contactstorage.model.User;
import com.contactstorage.repository.UserRepository;

@Controller
public class HomeController {
	
	public static String uploadDir=System.getProperty("user.dir")+ "/src/main/resources/static/productImages";
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	
	@GetMapping("/")
	 public String home(Model model) {
		model.addAttribute("title", "smart contact manager");
		return "home"; 
	 }
	
	@GetMapping("/signup")
	 public String signp(Model model) {
		model.addAttribute("title", "register - smart contact manager");
		model.addAttribute("User", new User());
		
		return "signup";
	}
	
	// handler for registering user
	// data ko accept krne ke liye @ModelAttribute form se
	// @RequestParam we use to accept data which is not there in User obejct , for agreement
	// Model is used to send data
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("User") User user,@RequestParam("profileImage")MultipartFile file,
			BindingResult result, @RequestParam(value="agreement" 
	, defaultValue="false")boolean agreement,Model model, HttpSession session) {
			
		try {
			
			if(!agreement) {
				System.out.println("you have not agreed to terms and conditions");
				throw new Exception("You have not agreed to terms and conditions");
			}
			
			if(result.hasErrors()){
				
				model.addAttribute("User",user);
				throw new Exception("fill the form correctly");
				
			}
			
			if(file.isEmpty()) {
				user.setImageurl("default.png");
				
			}
			else
			{
				
			Path fileNameAndPath=Paths.get(uploadDir,file.getOriginalFilename());
			Files.write(fileNameAndPath,file.getBytes());
			user.setImageurl(file.getOriginalFilename());
			
			}
			
			user.setRole("USER_ROLE");
			user.setEnabled(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			
			model.addAttribute("User", new User());
			userRepository.save(user);
			
			// if you want success in gren clour use alert-success for type
			session.setAttribute("message", new Message("Registered successfully !!" , "alert-success"));
			
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
			model.addAttribute("User",user);
	// if you want error in red colour you have to use alert-danger for type
			session.setAttribute("message", new Message("something went wrong !!   "+e.getMessage(), "alert-danger"));
			
			
		}
		return "signup";
		
		
		
	}
	
	@GetMapping("/login")
	public String customlogin() {
		return "login";
	}
	
}
