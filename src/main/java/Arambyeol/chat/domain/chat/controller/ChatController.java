package Arambyeol.chat.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Arambyeol.chat.domain.chat.dto.Response;
import Arambyeol.chat.domain.chat.dto.SendMessage;
import Arambyeol.chat.domain.chat.enums.Status;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

	@MessageMapping("/chat")
	@SendTo("/sub/ArambyeolChat")
	public Response chat(@RequestBody SendMessage message ){
		return new Response(Status.SUCCESS,message,null);
	}
}
