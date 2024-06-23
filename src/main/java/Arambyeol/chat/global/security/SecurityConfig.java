package Arambyeol.chat.global.security;

import Arambyeol.chat.global.security.handler.SecurityAccessDeniedHandler;
import Arambyeol.chat.global.security.handler.SecurityAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Arambyeol.chat.global.jwt.JwtAuthFilter;
import Arambyeol.chat.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenUtil jwtTokenUtil;
	private final SecurityAccessDeniedHandler securityAccessDeniedHandler;
	private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;
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
				.requestMatchers("/signUp").permitAll()
				.requestMatchers("/ws-stomp").permitAll().requestMatchers("/error").permitAll()
				.anyRequest().authenticated()
			)
				.exceptionHandling((handler) -> handler
						// 인증이 완료되었으나 해당 엔드포인트에 접근할 권한이 없는 경우
						.accessDeniedHandler(securityAccessDeniedHandler)
						// 인증이 필요하나 인증되지 않은 사용자가 접근하는 경우
						.authenticationEntryPoint(securityAuthenticationEntryPoint));
		return httpSecurity.build();
	}

}
