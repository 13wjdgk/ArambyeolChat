package Arambyeol.chat.domain.auth.service;

import Arambyeol.chat.global.exception.CustomException;
import Arambyeol.chat.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.auth.dto.DeviceId;
import Arambyeol.chat.domain.auth.dto.JwtToken;
import Arambyeol.chat.domain.chat.entity.DeviceInfo;
import Arambyeol.chat.domain.chat.repository.DeviceInfoRepository;
import Arambyeol.chat.domain.chat.service.DeviceInfoService;
import Arambyeol.chat.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {
	private final JwtTokenUtil jwtTokenUtil;
	private final DeviceInfoRepository deviceInfoRepository;
	private final JwtTokenRedisService jwtTokenRedisService;
	private final DeviceInfoService deviceInfoService;

	public JwtToken login(String deviceId) {
		String userDeviceId = isValidDeviceId(deviceId);
		JwtToken jwtToken = new JwtToken(createAccessToken(userDeviceId), createRefreshToken(userDeviceId));
		jwtTokenRedisService.saveRefreshToken(userDeviceId, jwtToken.refreshToken());
		return jwtToken;
	}
	public DeviceId signUp(DeviceId deviceId) {
		try{
			isValidDeviceId(deviceId.deviceId());
		}catch (CustomException e) {
			if (e.getErrorCode() != ErrorCode.NOT_FOUND_USER) {
				DeviceInfo deviceInfo = deviceInfoService.saveDeviceInfo(deviceId.deviceId());
				return new DeviceId(deviceInfo.getDeviceId());
			}
		}
		throw new CustomException(ErrorCode.EXIST_USER);
	}
	public String isValidDeviceId(String deviceId) {
		String userDeviceId = deviceInfoRepository.findDeviceInfoByDeviceId(deviceId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER)).getDeviceId();
		return userDeviceId;
	}
	public String createAccessToken(String deviceId) {
		return jwtTokenUtil.createAccessToken(deviceId);
	}
	public String createRefreshToken(String deviceId) {
		return jwtTokenUtil.createRefreshToken(deviceId);
	}
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
		throw new CustomException(ErrorCode.INVALID_TOKEN);
	}
}
