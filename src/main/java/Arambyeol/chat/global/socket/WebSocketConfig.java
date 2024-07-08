package Arambyeol.chat.global.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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
		//해당 prefix를 구독하는 클라이언트에게 메세지를 보낸다.
		registry.enableStompBrokerRelay("/sub")
			.setRelayHost(host).setRelayPort(port)
			.setSystemLogin(user).setSystemPasscode(password)
			.setClientLogin(user).setClientPasscode(password)
			.setUserDestinationBroadcast("/receipt");

		// 메세지 발행 요청 prefix, /pub으로 시작하는 메세지만 해당 Broker에서 받아와서 처리한다.
		registry.setApplicationDestinationPrefixes("/pub");

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
