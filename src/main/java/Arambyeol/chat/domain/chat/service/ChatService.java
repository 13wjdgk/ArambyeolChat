package Arambyeol.chat.domain.chat.service;

import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.dto.ReceiveMessage;
import Arambyeol.chat.domain.chat.dto.SendMessage;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final DeviceInfoService deviceInfoService;
	public ReceiveMessage makeChatMessage(SendMessage message){
		String nickname = deviceInfoService.getNickname(message.senderDid()).getNickname();

		return new ReceiveMessage(message,nickname);
	}
}
