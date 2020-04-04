package com.reservation.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	private final UserDetailsService userDetailsService;
	
	private JwtRequestFilter jwtRequestFilter;
	PasswordEncrypt passwordEncrypt;
	

	public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, 
			UserDetailsService userDetailsService,
			JwtRequestFilter jwtRequestFilter,
			PasswordEncrypt passwordEncrypt
			
			) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.userDetailsService = userDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
		this.passwordEncrypt = passwordEncrypt;
	}
	
	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	private BCryptPasswordEncoder passwordEncoder() {
		return this.passwordEncrypt.passwordEncoder();
	}
	
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//Configure AuthenticationManager so that it knows from where to load
		//User matching credentials 
		//User BCryptPasswordEncoder
		
		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers("/api/user")
			.permitAll()
			.antMatchers("/api/user/checkuniqueuser")
			.permitAll()
			.antMatchers("/api/user/checkavailableemail")
			.permitAll()
			.antMatchers("/api/user/sendtoken")
			.permitAll()
			.antMatchers("/api/user/validatetoken")
			.permitAll()
			.antMatchers("/api/user/resetpassword")
			.permitAll()
			.antMatchers("/api/auth/**")
			.permitAll()
			.antMatchers("/api/public/**")
			.permitAll()
			.antMatchers("/h2-console/**", "/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
			.permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/**")
			.permitAll()
			.antMatchers("/favicon.io",
					"/**/*.png",
					"/**/*.gif",
					"/**/*.html",
					"/**/*.jpg",
					"/**/*.css",
					"/**/*.js")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	
	
	
	
	
	
}
