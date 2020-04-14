package com.reservation.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.reservation.securityconfig.oauth.CustomOauth2UserService;
import com.reservation.securityconfig.oauth.HttpCookieOAuth2AuthoriationRequestRepository;
import com.reservation.securityconfig.oauth.OAuth2AuthenticationFailureHandler;
import com.reservation.securityconfig.oauth.OAuth2AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomOauth2UserService customOauth2UserService;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Autowired
	private HttpCookieOAuth2AuthoriationRequestRepository httpCookieOAuth2AuthoriationRequestRepository;

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	private final UserDetailsService userDetailsService;

	private JwtRequestFilter jwtRequestFilter;
	PasswordEncrypt passwordEncrypt;

	public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
			UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, PasswordEncrypt passwordEncrypt

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

	@Bean
	public HttpCookieOAuth2AuthoriationRequestRepository cookieAuthorizationRequestRepository() {
		return this.httpCookieOAuth2AuthoriationRequestRepository;
	}

	private BCryptPasswordEncoder passwordEncoder() {
		return this.passwordEncrypt.passwordEncoder();
	}

	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Configure AuthenticationManager so that it knows from where to load
		// User matching credentials
		// User BCryptPasswordEncoder

		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.exceptionHandling()
				.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
				.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin()
				.disable()
				.httpBasic()
				.disable().
			authorizeRequests()
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
				.antMatchers("/auth/**")
				.permitAll()
				.antMatchers("/login/oauth2/**")
				.permitAll()
				.antMatchers("/api/public/**")
				.permitAll()
				.antMatchers("/h2-console/**", "/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
				.permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**")
				.permitAll()
				.antMatchers("/favicon.io", "/**/*.png", "/**/*.gif", "/**/*.html", "/**/*.jpg", "/**/*.css",
						"/**/*.js")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.oauth2Login()
				.authorizationEndpoint()
				.baseUri("/oauth2/authorize")
				.authorizationRequestRepository(cookieAuthorizationRequestRepository())
				.and()
				.redirectionEndpoint().baseUri("/oauth2/callback/**")
				.and().
				userInfoEndpoint()
				.userService(oAuth2UserService())
				.and()
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		enhanceJsonMessageConverter(restTemplate);
		this.customOauth2UserService.setRestOperations(restTemplate);
		return this.customOauth2UserService;
	}

	private void enhanceJsonMessageConverter(RestTemplate restTemplate){
		HttpMessageConverter<?> jsonMessageConverter = restTemplate.getMessageConverters()
					.stream()
					.filter(c -> c instanceof MappingJackson2HttpMessageConverter)
					.findFirst()
					.orElse(null);
		if(jsonMessageConverter == null){
			return;
		}
		List<MediaType> supportedMediaTypes = new ArrayList<>(jsonMessageConverter.getSupportedMediaTypes());
		supportedMediaTypes.add(MediaType.valueOf("text/javascript;charset=UTF-8"));
		((AbstractHttpMessageConverter)jsonMessageConverter).setSupportedMediaTypes(supportedMediaTypes);
	}

}
