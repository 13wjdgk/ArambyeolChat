package Arambyeol.chat.domain.chat.entity;


import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Document
public class MainChat {
	@Id
	private String id;
	private String senderDid;
	private String message;
	private String sederNickname;
	private LocalDateTime sendTime;

	@Builder
	public MainChat(String senderDid, String message, String sederNickname, LocalDateTime sendTime){
		this.senderDid = senderDid;
		this.sederNickname = sederNickname;
		this.sendTime = sendTime;
		this.message = message;

	}
}
