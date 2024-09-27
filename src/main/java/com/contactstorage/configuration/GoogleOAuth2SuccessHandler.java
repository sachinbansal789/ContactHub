package com.contactstorage.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
 import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contactstorage.model.User;
import com.contactstorage.repository.UserRepository;


@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	 private UserRepository userRepository;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequestrequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		// we have to upcast authentication
		OAuth2AuthenticationToken token =(OAuth2AuthenticationToken) authentication;
		
		// we will use this get principal method in OAuth cases , extraction email from token
		String email = token.getPrincipal().getAttributes().get("email").toString();
		
		
		// if details of user is present inside database we will not do anything otherwise we have to add the details from token to database
		// we are checking inside userrepo by email
		if(userRepository.findUserByEmail(email) != null) {
			
		}else {
			User user = new User();
			
			// whenevr we use object of httpservlert we use get.Principal  .getattributes to extract data
			user.setName(token.getPrincipal().getAttributes().get("given_name").toString());
			
			user.setEmail(email);
			
			user.setRole("USER_ROLE");
		    
	     	userRepository.save(user);
	}
		
		redirectStrategy.sendRedirect(httpServletRequestrequest, httpServletResponse, "/user/index");
		
	}

}
