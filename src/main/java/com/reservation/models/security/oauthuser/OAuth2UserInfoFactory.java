package com.reservation.models.security.oauthuser;

import java.util.Map;


import com.reservation.exception.OAuth2AuthenticationProcessingException;
import com.reservation.models.security.AuthProvider;

public class OAuth2UserInfoFactory {
	
	
	public static OAuth2UserInfo geAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
			return new GoogleOAuth2UserInfo(attributes);
			
		}
		else if(registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
			return new FacebookOAuth2UserInfo(attributes);
		}
		else {
			throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet");
			
		}
	}

}
