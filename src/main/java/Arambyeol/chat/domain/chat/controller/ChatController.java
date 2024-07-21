package Arambyeol.chat.domain.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import Arambyeol.chat.domain.chat.dto.ReportChat;
import Arambyeol.chat.domain.chat.dto.SendMessage;
import Arambyeol.chat.global.dto.SuccessSingleResponse;
import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.entity.Report;
import Arambyeol.chat.domain.chat.service.ChatService;
import Arambyeol.chat.domain.chat.service.DeviceInfoService;
import Arambyeol.chat.domain.chat.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class ChatController {
	private final RabbitTemplate template;
	private final SimpMessagingTemplate messagingTemplate;

	private final DeviceInfoService deviceInfoService;
	private final ReportService reportService;
	private final ChatService chatService;

	@GetMapping("/nickname")
	public ResponseEntity<SuccessSingleResponse<DeviceInfo>> getUserNickname(@AuthenticationPrincipal UserDetails userDetails){
		DeviceInfo deviceInfo = deviceInfoService.getDeviceInfo(userDetails.getUsername());
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), deviceInfo));
	}

	@PostMapping("/reportChat")
	public ResponseEntity<SuccessSingleResponse<Report>> reportChat( @RequestBody ReportChat reportChat){
		Report reportResult = reportService.postReport(reportChat);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), null));
	}

	@MessageMapping("chat.message.{chatType}")
	public void chat(@RequestBody SendMessage message , @DestinationVariable String chatType , SimpMessageHeaderAccessor headerAccessor){
		ReceiveMessage receiveMessage = chatService.createChatMessage(message);
		String receiptId = headerAccessor.getFirstNativeHeader("receipt");
		if (receiptId != null) {
			StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.SEND);
			stompHeaderAccessor.setReceiptId(receiptId);
			messagingTemplate.convertAndSend("/queue/type.receipt-user"+headerAccessor.getSessionId(),"",stompHeaderAccessor.getMessageHeaders());
		}
		log.info("message 전달 : "+receiveMessage.toString());
		template.convertAndSend("amq.topic", "type." + chatType, receiveMessage); //topic
	}

	@GetMapping("/chatList")
	public ResponseEntity<SuccessSingleResponse<List<ReceiveMessage>>> getUserNickname(@RequestParam LocalDateTime start ,int page,int size){
		List<ReceiveMessage> chatList = chatService.findRecentChatMessage(start,page,size);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), chatList));
	}
}
