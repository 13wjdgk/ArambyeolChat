package Arambyeol.chat.global.security;

import java.util.Optional;

import Arambyeol.chat.global.exception.CustomException;
import Arambyeol.chat.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
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
public class JwtChannelInterceptor implements ExecutorChannelInterceptor {
	@Autowired
	@Lazy
	private SimpMessagingTemplate messagingTemplate;
	private final String AUTHORIZATION = "Authorization";
	private final JwtTokenUtil jwtTokenUtil;
	private final CustomUserDetailsService customUserDetailsService;
	//채널에 전송되기 전에 호출
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("JwtChannelInterceptor - preSend 호출");

		// 메세지 헤더 접근
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if(!StompCommand.CONNECT.equals( accessor.getCommand())&&!StompCommand.SUBSCRIBE.equals( accessor.getCommand())){
			return message;
		}
		String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);
		log.info("headers : "+ accessor);
		log.info("accessToken : " + accessToken);

		if(accessToken != null && accessToken.startsWith("Bearer ")) {
			try{
				jwtTokenUtil.isValidToken(accessToken);
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtTokenUtil.getDeviceId(accessToken.substring(7)));
				if(userDetails != null) {
					//인증 토큰 생성
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					//SecurityContextHolder에 인증 정보 저장
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					log.info("인증완료");
				}
			}catch (CustomException e){
				log.error("error : "+e.getMessage());
				throw new CustomException(ErrorCode.INVALID_TOKEN);
			}

		}else{
			log.error("error : 토큰이 없습니다.");
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}

		return message;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (sent&&accessor.getCommand().equals(StompCommand.SEND)) {
			sendReceipt(message);
		}
	}

	private void sendReceipt(Message<?> message) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor != null) {
			String sessionId = accessor.getSessionId();
			// RECEIPT 프레임 생성
			if (sessionId != null ) {
				String receiptMessage = "RECEIPT Message " ;
				messagingTemplate.convertAndSendToUser(sessionId, "/receipt", receiptMessage,createHeaders(sessionId));
			}
		}
	}
	private MessageHeaders createHeaders(String sessionId) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
		if (sessionId != null) headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}
}
