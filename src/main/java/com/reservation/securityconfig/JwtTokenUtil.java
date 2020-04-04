package com.reservation.securityconfig;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.reservation.models.security.BookingUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1359455143018564846L;
	
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secret;
	
	
	//retrieve user name from JWT token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims :: getSubject) ;
	}
	
	//retrieve expiration date from jwtToken
	public Date getExpirationDateFromDToken(String token) {
		return getClaimFromToken(token, Claims :: getExpiration);
	}
	
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//For retrieving any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	//Check if token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromDToken(token);
		return expiration.before(new Date());
	}
	
	//Generate token for user
	public String generateToken(BookingUser userDetails) {
		Map<String, Object> claims = new HashMap<String, Object>();
		return doGenerateToken(claims, userDetails.getEmail());
	}

	/*
	 * While creating the token -
	 * 1. Define claims of the token, like issuer, expiration, subject, and the id
	 * 2. Sign the JWT using the HS512 algorithm and secret key.
	 * 3. According to JWS compact serialization
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	
	public Boolean validateToken(String token, BookingUser userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
	}
	
	
}
