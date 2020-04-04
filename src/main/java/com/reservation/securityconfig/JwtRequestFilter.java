package com.reservation.securityconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.reservation.models.security.BookingUser;
import com.reservation.services.BookingUserService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	
	private final BookingUserService userService;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	public JwtRequestFilter(BookingUserService userService, JwtTokenUtil jwtTokenUtil) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwtToken = null;
		
		//JWT token is in the form "Bearer token". Remove Bearer word and get only the token
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				username = this.jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			}
			catch (ExpiredJwtException e) {
				response.sendError(401, "Token has expired");
				System.out.println("JWT Token has expired");
				return;
			}
		}
		else {
			LOGGER.warn("JWT Token does not begin with Bearer String.");
		}
		
		//Once we get we validate the token
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final BookingUser userDetails = this.userService.loadUserByEmail(username);
			
			//If token is valid configure Spring Security to manually set
			if(this.jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				
				usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				/*
				 * After setting the Authentication in the context, we specify
				 * that the current user is authenticated. So it passes the Spring 
				 * security configurations successfully.
				 */
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);		
	}

}
