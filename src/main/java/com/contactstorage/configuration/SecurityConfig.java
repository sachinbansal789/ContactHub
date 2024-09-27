

package com.contactstorage.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends   WebSecurityConfigurerAdapter {
	
	
	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
	
//	@Autowired
//	CustomUserDetailsService customUserDetailService;
//	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		  
		     .authorizeRequests()
		     .antMatchers("/h2-console/**").permitAll()
		     .antMatchers("/" , "/shop/**" ,  "/register/**" ).permitAll()
		//     .antMatchers("/admin").hasRole("ADMIN")
		     .antMatchers("/admin/**").permitAll()
		//     .antMatchers("/admin").hasRole("ADMIN")
		      .antMatchers("/User/**").hasAuthority("USER")
		     .anyRequest()
		     .authenticated()
		     
		     
		     .and()
		     .formLogin()
		     .loginPage("/login")
		     .permitAll()
		//     .usernameParameter("email")
		//     .passwordParameter("password")
		     .failureUrl("/login?error=true")
		     .defaultSuccessUrl("/user/index")
		     
		     .and()
		     .oauth2Login()
		     .loginPage("/login")
		     .successHandler(googleOAuth2SuccessHandler)
		      
		     
		    .and()
		    .logout()
		    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		    .logoutSuccessUrl("/login")
		    
		    .invalidateHttpSession(true)
		    // jsession name of the place where cookies are present
		    .deleteCookies("JSESSIONID")
		    .and()
		    .exceptionHandling()
		    .and()
		    .csrf()
		    .disable()
		   
		    
		    // If you choose to disable the X-Frame-Options header 
		    //(not recommended) by setting . headers(). frameOptions(). disable() ,
		    //then Spring Security will not add the X-Frame-Options header to the response.
		    //This means your application could be rendered in a frame, and also could be vulnerable to Clickjacking attacks.
		    .headers().frameOptions().disable()
		  		     ;
	}
	
	
	// now the password will be checked in an ecrypted form
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public CustomUserDetailsService customUserDetailService() {
		return new CustomUserDetailsService();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService());
		

	
}
	// extra security check for data which is stored somewhere, like where image is stored , for resources
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**" , "/static/**" , "/images/**" ,"/productImages/**", "/css/**" , "/js/**" );
		
	}
}
