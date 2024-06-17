package Arambyeol.chat.domain.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.auth.dto.JwtToken;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import Arambyeol.chat.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {
	private final JwtTokenUtil jwtTokenUtil;
	private final DeviceInfoRepository deviceInfoRepository;
	private final JwtTokenRedisService jwtTokenRedisService;
	public JwtToken generateNewAccessToken(String refreshToken) {
		if(refreshToken != null && refreshToken.startsWith("Bearer")) {
			String refreshTokenValue = refreshToken.substring(7);
			jwtTokenUtil.validateToken(refreshTokenValue);
			String deviceId = jwtTokenUtil.getDeviceId(refreshTokenValue);
			if(jwtTokenRedisService.isValidRefreshToken(deviceId, refreshTokenValue)) {
				jwtTokenRedisService.deleteRefreshToken(refreshTokenValue);
				return login(deviceId);
			}
		}
		throw new UsernameNotFoundException("Invalid token");
	}
	public JwtToken login(String deviceId) {
		String userDeviceId = isValidDeviceId(deviceId);
		JwtToken jwtToken = new JwtToken(createAccessToken(userDeviceId), createRefreshToken(userDeviceId));
		jwtTokenRedisService.saveRefreshToken(userDeviceId, jwtToken.refreshToken());
		return jwtToken;
	}
	public String isValidDeviceId(String deviceId) {
		String userDeviceId = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(()->new UsernameNotFoundException("No value deviceInfo")).getDeviceId();
		return userDeviceId;
	}
	public String createAccessToken(String deviceId) {
		return jwtTokenUtil.createAccessToken(deviceId);
	}
	public String createRefreshToken(String deviceId) {
		return jwtTokenUtil.createRefreshToken(deviceId);
	}
}
