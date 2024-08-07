package Arambyeol.chat.domain.chat.service;

import Arambyeol.chat.global.exception.CustomException;
import Arambyeol.chat.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import Arambyeol.chat.domain.chat.repository.PrefixesRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class DeviceInfoService {
	private final DeviceInfoRepository deviceInfoRepository;
	private final PrefixesRepository prefixesRepository;
	private final String[] characters = new String[]{"금이","지누","아람이","긱사생","별이","동식이"};

	public DeviceInfo getDeviceInfo(String deviceId){
		DeviceInfo deviceInfo = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));
		return deviceInfo;

	}
	public DeviceInfo saveDeviceInfo(String deviceId){
		String nickname = createNickname();
		DeviceInfo deviceInfo = DeviceInfo.builder().deviceId(deviceId).nickname(nickname).build();
		return deviceInfoRepository.save(deviceInfo);
	}
	public String createNickname() {
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		String prefix = prefixesRepository.findPrefixesById(randomNumber).orElseThrow(()->new NoSuchElementException("No value prefix")).getPrefix();
		String characterName = characters[randomNumber%6];
		String nickname = String.join(" ", prefix, characterName);
		int cntNickname = deviceInfoRepository.countByNicknameContaining(nickname);
		if(cntNickname>=1){
			nickname = String.join("", nickname, String.valueOf(cntNickname+1));
		}
		return nickname;
	}
}
