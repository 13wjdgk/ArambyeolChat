package Arambyeol.chat.domain.chat.dto;

import java.time.LocalDateTime;

public record ReceiveMessage(String senderDid , String senderNickname ,String chatId, String message, LocalDateTime sendTime) {
	public ReceiveMessage(SendMessage message , String senderNickname,String chatId){
		this(message.senderDid(),senderNickname,chatId,message.message(),message.sendTime());

	}
}
