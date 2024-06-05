package Arambyeol.chat.domain.chat.service;

import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.dto.NicknameResponse;
import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class DeviceInfoService {
	private final DeviceInfoRepository deviceInfoRepository;
	public DeviceInfo getNickname(String deviceId){
		DeviceInfo deviceInfo;
		try{
			deviceInfo = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(IllegalStateException::new);
		}catch (IllegalStateException e){
			String nickname = "아람이_"+deviceId;
			deviceInfo = deviceInfoRepository.save(DeviceInfo.builder().deviceId(deviceId).nickname(nickname).build());
		}
		return deviceInfo;

	}
}
