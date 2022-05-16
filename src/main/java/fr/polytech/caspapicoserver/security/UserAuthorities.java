package fr.polytech.caspapicoserver.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserAuthorities {
	LIST_OWN_DEVICES("LIST_OWN_DEVICES"),
	ADD_DEVICE("ADD_DEVICE"),

	WEBAPI_ACTIVATE_DEVICE("WEBAPI_ACTIVATE_DEVICE"),
	WEBAPI_DEVICE_INFO("WEBAPI_DEVICE_INFO");

	public final SimpleGrantedAuthority authority;
	private final String authorityStr;
	UserAuthorities(String authorityStr){
		this.authorityStr = authorityStr;
		authority = new SimpleGrantedAuthority(authorityStr);
	}


	@Override
	public String toString() {
		return authorityStr;
	}
}
