package Arambyeol.chat.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		System.out.println("WebSocketSecurityConfig");
		messages
				//connect 허용 (목적지가 null인 경우)
				.nullDestMatcher().permitAll()
				// 특정 목적지
				.simpDestMatchers("/pub/chat").permitAll()
				// 구독
				.simpSubscribeDestMatchers("/sub/ArambyeolChat").permitAll()
				.anyMessage().denyAll();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}
