package Arambyeol.chat.domain.chat.mapper;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import Arambyeol.chat.domain.chat.entity.MainChat;
import org.springframework.stereotype.Component;

@Component
public class ReceiveMessageMapper {

	public ReceiveMessage toReceiveMessage(MainChat mainChat) {
		return new ReceiveMessage(
			mainChat.getSenderDid(),
			mainChat.getSederNickname(),
			mainChat.getId(),
			mainChat.getMessage(),
			mainChat.getSendTime()
		);
	}
}
