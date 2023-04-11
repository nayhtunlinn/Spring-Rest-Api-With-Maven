package com.nay.spring.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Bean
	public UserDetailsManager userDetailManager() {
		return new JdbcUserDetailsManager(dataSource);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests(customizer->customizer
	    		.antMatchers(HttpMethod.GET,"/api/customers").hasAnyRole("EMPLOYEE","MANAGER","ADMIN")
	    		.antMatchers(HttpMethod.GET,"/api/customers/**").hasAnyRole("EMPLOYEE","MANAGER","ADMIN")
	    		.antMatchers(HttpMethod.POST,"/api/customers").hasAnyRole("MANAGER","ADMIN")
	    		.antMatchers(HttpMethod.PUT,"/api/customers").hasAnyRole("MANAGER","ADMIN")
	    		.antMatchers(HttpMethod.DELETE,"/api/customers/**").hasAnyRole("ADMIN")
	    		.anyRequest().authenticated()).formLogin();
	    
	}
}
