package com.retryreplayframwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//				.authorizeHttpRequests(auth -> auth.requestMatchers("/retry/replay").hasRole("ADMIN")
//						.requestMatchers("/retry/jobs").hasAnyRole("ADMIN", "USER")
//						.requestMatchers("/retry/**", "/replay/**").permitAll().anyRequest().authenticated());
//
//		return http.build();
//	}
//}

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and()
				.authorizeHttpRequests(authz -> authz.requestMatchers(HttpMethod.POST, "/retry/manual/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/retry/replay").permitAll().requestMatchers("/auth/**")
						.permitAll().requestMatchers("/retry/create").permitAll()
						.requestMatchers(HttpMethod.GET, "/retry/jobs").permitAll()
						.requestMatchers("/retry/create/**", "/replay/**").authenticated().anyRequest().permitAll());

		return http.build();
	}
}
