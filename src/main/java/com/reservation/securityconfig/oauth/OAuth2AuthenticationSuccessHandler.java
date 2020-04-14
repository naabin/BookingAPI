package com.reservation.securityconfig.oauth;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reservation.exception.OAuth2AuthenticationProcessingException;
import com.reservation.models.security.oauthuser.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.reservation.securityconfig.AppProperties;
import com.reservation.securityconfig.JwtTokenUtil;

import static com.reservation.securityconfig.oauth.HttpCookieOAuth2AuthoriationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private JwtTokenUtil tokenProvider;
	
	private AppProperties appProperties;
	
	private HttpCookieOAuth2AuthoriationRequestRepository httpCookieOAuth2AuthoriationRequestRepository;
	

	
	@Autowired
	public OAuth2AuthenticationSuccessHandler(JwtTokenUtil tokenProvider, AppProperties appProperties,
			HttpCookieOAuth2AuthoriationRequestRepository httpCookieOAuth2AuthoriationRequestRepository) {
		this.tokenProvider = tokenProvider;
		this.appProperties = appProperties;
		this.httpCookieOAuth2AuthoriationRequestRepository = httpCookieOAuth2AuthoriationRequestRepository;
	}


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		
		String targetUrl = determineTargetUrl(request, response, authentication);
		
		
		
		if(response.isCommitted()) {
			logger.debug("Response has already been commited. Unable to redirect to " + targetUrl);
			return;
		}
		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
		
	}
	
	
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie :: getValue);

		if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw new OAuth2AuthenticationProcessingException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with authentication.");
			
		}
		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
		
		String token = tokenProvider.generateToken(authentication);
		UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

			return UriComponentsBuilder.fromUriString(targetUrl)
					.queryParam("token", token)
					.queryParam("userId", user.getId())
					.build()
					.toUriString();



	}
	
	
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		this.httpCookieOAuth2AuthoriationRequestRepository.removeAuthorizationCookieRequests(request, response);
	}
	
	
	
	private boolean isAuthorizedRedirectUri(String uri) {
		URI clientRedirectUri = URI.create(uri);
		return appProperties.getoAuth2().getAuthorizedRedirectUris()
				.stream()
				.anyMatch(authorizedredirectUri -> {
					//Only Validate Host and Port, Let the client use different paths if they want to
					URI authorizedURI = URI.create(authorizedredirectUri);
					if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort()){
						return true;
						
					}
					return false;
				});
	}
	

}
