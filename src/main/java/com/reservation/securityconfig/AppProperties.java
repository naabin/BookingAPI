package com.reservation.securityconfig;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
	private final OAuth2 oAuth2 = new OAuth2();
	public static final class OAuth2{
		private List<String> authorizedRedirectUris = new ArrayList<String>();

		public List<String> getAuthorizedRedirectUris() {
			return authorizedRedirectUris;
		}

		public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
			this.authorizedRedirectUris = authorizedRedirectUris;
		}
		
	}

	public OAuth2 getoAuth2() {
		return oAuth2;
	}
	
	
	
}
