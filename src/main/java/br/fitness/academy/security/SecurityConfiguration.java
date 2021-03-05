package br.fitness.academy.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.authorizeRequests()
		.antMatchers("/").permitAll();//.authenticated()
		
		
		httpSecurity.csrf().disable();
		
		httpSecurity.headers()
		.frameOptions().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/static/**");
		
	}
}
