package Arambyeol.chat.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Arambyeol.chat.global.jwt.CustomUserDetailsService;
import Arambyeol.chat.global.jwt.JwtAuthFilter;
import Arambyeol.chat.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenUtil jwtTokenUtil;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.formLogin((formLogin) -> formLogin.disable())
			.httpBasic(AbstractHttpConfigurer::disable)
			.addFilterBefore(new JwtAuthFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/login").permitAll()
				.requestMatchers("/generateAccessToken").permitAll()
				.requestMatchers("/signUp").permitAll()
				.requestMatchers("/ws-stomp").permitAll()
				.anyRequest().authenticated()
			);
		return httpSecurity.build();
	}

}
