package Arambyeol.chat.global.security;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import Arambyeol.chat.global.jwt.CustomUserDetailsService;
import Arambyeol.chat.global.jwt.JwtAuthFilter;
import Arambyeol.chat.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
	private final String AUTHORIZATION = "Authorization";
	private final JwtTokenUtil jwtTokenUtil;
	private final CustomUserDetailsService customUserDetailsService;
	//채널에 전송되기 전에 호출
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("JwtChannelInterceptor - preSend 호출");
		// 메세지 헤더 접근
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);
		log.info("headers : "+accessor.toString());
		log.info("accessToken : " + accessToken);
		jwtTokenUtil.isValidToken(accessToken);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtTokenUtil.getDeviceId(accessToken.substring(7)));
		if(userDetails != null) {
			//인증 토큰 생성
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			//SecurityContextHolder에 인증 정보 저장
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			log.info("인증완료");
		}
		return message;
	}
}
