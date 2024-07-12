package Arambyeol.chat.global.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import Arambyeol.chat.global.security.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final JwtChannelInterceptor jwtChannelInterceptor;
	@Value("${spring.activemq.user}")
	private String user;
	@Value("${spring.activemq.password}")
	private String password;
	@Value("${spring.activemq.host}")
	private String host;
	@Value("${spring.activemq.port}")
	private int port;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry){
		registry.setPathMatcher(new AntPathMatcher("."));
		registry.setApplicationDestinationPrefixes("/pub")
			.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
			.setRelayHost("localhost")
			.setVirtualHost("/")
			.setRelayPort(61613)
			.setClientLogin("guest")
			.setClientPasscode("guest")
			.setSystemLogin("guest")
			.setSystemPasscode("guest");

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-stomp")
			.setAllowedOrigins("*");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(jwtChannelInterceptor);
	}


}
