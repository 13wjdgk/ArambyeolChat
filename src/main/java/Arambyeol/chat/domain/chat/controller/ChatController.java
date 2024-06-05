package Arambyeol.chat.domain.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import Arambyeol.chat.domain.chat.dto.ReportChat;
import Arambyeol.chat.domain.chat.dto.Response;
import Arambyeol.chat.domain.chat.dto.SendMessage;
import Arambyeol.chat.domain.chat.dto.SuccessSingleResponse;
import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.entity.Report;
import Arambyeol.chat.domain.chat.enums.Status;
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
	@Autowired
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final DeviceInfoService deviceInfoService;
	private final ReportService reportService;
	private final ChatService chatService;
	@GetMapping("/nickname")
	public ResponseEntity<SuccessSingleResponse<DeviceInfo>> getUserNickname(@RequestParam String deviceId){
		DeviceInfo deviceInfo = deviceInfoService.getNickname(deviceId);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), deviceInfo));
	}

	@PostMapping("/reportChat")
	public ResponseEntity<SuccessSingleResponse<Report>> reportChat( @RequestBody ReportChat reportChat){
		Report reportResult = reportService.postReport(reportChat);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), null));
	}

	// @MessageMapping("/chat")
	// public void chat(@RequestBody SendMessage message , Principal principal ){
	// 	String sessionId = principal.getName();
	// 	log.info("Session ID 처음 : " + sessionId);
	// 	ReceiveMessage receiveMessage = chatService.makeChatMessage(message);
	// 	log.info("message 전달 : "+receiveMessage.toString());
	// 	sessionId = principal.getName();
	// 	log.info("Session ID 중간 : " + sessionId);
	// 	simpMessagingTemplate.convertAndSend("/sub/ArambyeolChat","메세지가냐고");
	// 	sessionId = principal.getName();
	// 	log.info("Session ID 보내고 나서: " + sessionId);
	// }
	@MessageMapping("/chat")
	@SendTo("/sub/ArambyeolChat")
	public Response chat(@RequestBody SendMessage message ){
		return new Response(Status.SUCCESS,message,null);
	}
}
