package Arambyeol.chat.global.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/ws-stomp") // WebSocket endpoint
					.allowedOrigins("*") // 허용할 출처
					.allowedMethods("GET", "POST") // 허용할 HTTP 메서드
					.allowedHeaders("*") // 허용할 요청 헤더
					.exposedHeaders("Custom-Header"); // 클라이언트에 노출할 응답 헤더
			}
		};
	}
}
