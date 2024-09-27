package com.contactstorage.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactstorage.Service.EmailService;
import com.contactstorage.model.User;
import com.contactstorage.repository.UserRepository;

@Controller
public class ForgotController{
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	
	@GetMapping("/forgot")
	public String openEmailForm() {
		
		
		return "forgot_email_form";
	}
	
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email")String email, HttpSession session) {
		
		// 1000 is min value of otp ,where 1000 is exclusive
		
		Random random = new Random(1000);
		
		// 999999 is mac value of otp
		int otp=random.nextInt(999999);
		
		String from ="sachinbansal789@gmail.com";
		
		String subject ="OTP from SCM";
		
		String message = "OTP = "+otp+"";
		
		String to = email;
		
		boolean flag =emailService.sendEmail(from, message, to, subject);
		
		if(flag) {
			  
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp" ;
							
		}
		else
		{
			session.setAttribute("message", "check your email id");
			
			
			return "forgot_email_form";
		}
		
	
	}
	
	@PostMapping("/verify_otp")
	public String verifyOTP(@RequestParam("otp")int otp ,HttpSession session) {
		
		int oldotp = (int) session.getAttribute("myotp");
		
		String email =(String) session.getAttribute("email");
		
		if(oldotp ==otp) {
			session.setAttribute("message", "OTP verified");
			
			User user =userRepository.findUserByEmail(email);
			
			if(user == null) {
				
				session.setAttribute("message", "user with email id does not exist");
				
				return "forgot_email_form";
				
			}
			else {
			
			return "password_change_form";
		}
		}
		else {
			session.setAttribute("message","invalid otp" );
			return "verify_otp";
		}
	}
	
	
	@PostMapping("/change-password")
	public String SaveNewPassword(@RequestParam("newpassword")String pass,HttpSession session) {
		
		String email =(String) session.getAttribute("email");
		
		User user =userRepository.findUserByEmail(email);
		
		user.setPassword(bcryptPasswordEncoder.encode(pass));
		
		userRepository.save(user);
		
		
		return "redirect:/index";
		
	}
	
	
	
	
	
}