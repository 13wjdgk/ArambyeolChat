package Arambyeol.chat.global.jwt;

import Arambyeol.chat.global.exception.CustomException;
import Arambyeol.chat.global.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final DeviceInfoRepository deviceInfoRepository;
	@Override
	public UserDetails loadUserByUsername(String deviceId) {
		DeviceInfo deviceInfo = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));
		return new CustomUserDetails(deviceInfo.getDeviceId());
	}
}
