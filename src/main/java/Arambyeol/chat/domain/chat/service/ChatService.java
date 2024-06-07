package Arambyeol.chat.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import Arambyeol.chat.domain.chat.dto.SendMessage;
import Arambyeol.chat.domain.chat.entity.MainChat;
import Arambyeol.chat.domain.chat.mapper.ReceiveMessageMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	@Autowired
	private MongoTemplate mongoTemplate;
	private final ReceiveMessageMapper receiveMessageMapper;
	private final DeviceInfoService deviceInfoService;
	public ReceiveMessage createChatMessage(SendMessage message){
		String nickname = deviceInfoService.getNickname(message.senderDid()).getNickname();
		MainChat chat = saveChatMessage(message.senderDid(),message.message(),nickname,message.sendTime());
		return new ReceiveMessage(message,nickname,chat.getId());
	}
	public MainChat saveChatMessage(String senderDid, String message, String sederNickname, LocalDateTime sendTime){
		return mongoTemplate.save(MainChat.builder().message(message).sederNickname(sederNickname).senderDid(senderDid).sendTime(sendTime).build());
	}

	public List<ReceiveMessage> findRecentChatMessage(LocalDateTime start,int page, int size){
		Query query = new Query().addCriteria(Criteria.where("sendTime").lte(start)).with(Sort.by(Sort.Direction.DESC,"sendTime")).skip((page-1)*size).limit(size);
		List<MainChat> chatList = mongoTemplate.find(query,MainChat.class);
		return chatList.stream().map(receiveMessageMapper::toReceiveMessage).collect(Collectors.toList());
	}
}
