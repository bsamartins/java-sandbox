package org.bmartins.springangularjssecurity;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StartApplication 
	//extends WebSecurityConfigurerAdapter 
{
	
	@RequestMapping("api/user")
	public Principal user(Principal user) {
	    return user;
	}
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	      http
	      	.httpBasic()	      	
	      .and()
	        .authorizeRequests()
	          .antMatchers("/**.html", "/api/user").permitAll()
	          .anyRequest().authenticated();
		}
	}
	
	@Bean
	public AuthenticationManager authenticationManager(ObjectPostProcessor<Object> objectPostProcessor) throws Exception {

		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = 
				new AuthenticationManagerBuilder(objectPostProcessor)
			.inMemoryAuthentication();
	            
		builder
			.withUser("admin").password("password").roles( "USER" );

		return builder.and().build();
	}
	
	public static void main(String args[]) throws Exception {
		SpringApplication.run(StartApplication.class, args);	
	}

}