package fr.polytech.caspapicoserver.security;
import fr.polytech.caspapicoserver.security.UserAuthorities;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserRoles {
	UTILISATEUR(Arrays.asList(UserAuthorities.LIST_OWN_DEVICES, UserAuthorities.ADD_DEVICE, UserAuthorities.WEBAPI_ACTIVATE_DEVICE, UserAuthorities.WEBAPI_DEVICE_INFO)),
	ADMINISTRATEUR(Stream.concat(Stream.of(/*INSERT HERE*/), UTILISATEUR.authorities.stream()).collect(Collectors.toList()));

	public final Collection<UserAuthorities> authorities;
	UserRoles(Collection<UserAuthorities> authorities){
		this.authorities = authorities;
	}

	public Collection<GrantedAuthority> grantedAuthorities(){
		return authorities.stream().map(it -> it.authority).collect(Collectors.toList());
	}
}
