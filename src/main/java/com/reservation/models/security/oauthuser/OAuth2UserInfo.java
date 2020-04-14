package com.reservation.models.security.oauthuser;

import java.util.Map;

public abstract class OAuth2UserInfo {
	
	
	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		super();
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public abstract String getId();
	
	public abstract String getName();
	
	public abstract String getEmail();
	
	public abstract String getImageUrl();

}
