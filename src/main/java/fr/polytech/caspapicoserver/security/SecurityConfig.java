package fr.polytech.caspapicoserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
		return http.authorizeExchange()
				.pathMatchers("/webapi/device/activate").hasAuthority(UserAuthorities.WEBAPI_ACTIVATE_DEVICE.toString())
				.pathMatchers("/webapi/device/info").hasAuthority(UserAuthorities.WEBAPI_DEVICE_INFO.toString())
				.pathMatchers("/ajout-appareil").hasAuthority(UserAuthorities.ADD_DEVICE.toString())
				.pathMatchers("/mes-appareils").hasAuthority(UserAuthorities.LIST_OWN_DEVICES.toString())

				.pathMatchers("/**").permitAll()
				.anyExchange().authenticated()
				.and().csrf().requireCsrfProtectionMatcher(new ServerWebExchangeMatcher() {
					@Override
					public Mono<MatchResult> matches(ServerWebExchange exchange) {
						return ServerWebExchangeMatchers.pathMatchers("/api/**").matches(exchange).flatMap(it -> {
							if(it.isMatch()){
								return MatchResult.notMatch();
							}
							return ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/**").matches(exchange);
						});
					}
				})
				.and().formLogin()
				.and().logout()
				.and().build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
