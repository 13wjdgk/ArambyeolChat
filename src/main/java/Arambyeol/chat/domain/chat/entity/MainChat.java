package Arambyeol.chat.domain.chat.entity;


import jakarta.persistence.Id;

// @Document(collation = "MainChat")
public class MainChat {
	@Id
	private String chatId;
	private String senderDid;
	private String sederNickname;
	private String sendTime;
}
