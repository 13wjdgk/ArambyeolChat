package Arambyeol.chat.domain.chat.dto;

import java.time.LocalDateTime;

public record ReceiveMessage(String senderDid , String senderNickname , String message, LocalDateTime sendTime) {
	public ReceiveMessage(SendMessage message , String senderNickname){
		this(message.senderDid(),senderNickname,message.message(),message.sendTime());

	}
}
