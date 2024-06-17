package Arambyeol.chat.global.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final DeviceInfoRepository deviceInfoRepository;
	@Override
	public UserDetails loadUserByUsername(String deviceId) throws UsernameNotFoundException {
		DeviceInfo deviceInfo = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(()->new UsernameNotFoundException("No value deviceInfo"));
		return new CustomUserDetails(deviceInfo.getDeviceId());
	}
}
