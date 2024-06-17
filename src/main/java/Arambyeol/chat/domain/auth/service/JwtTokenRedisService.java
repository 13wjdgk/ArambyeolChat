package Arambyeol.chat.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.auth.entity.RefreshToken;
import Arambyeol.chat.domain.auth.repository.RefreshTokenRepository;

@Service
public class JwtTokenRedisService {
	private final RefreshTokenRepository refreshTokenRepository;

	private final Long refreshTokenExpirationTime ;

	public JwtTokenRedisService(RefreshTokenRepository refreshTokenRepository, @Value("${jwt.RefreshExpire}") Long refreshTokenExpirationTime) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime * 1000L;
	}

	public RefreshToken saveRefreshToken(String deviceId, String token) {
		return refreshTokenRepository.save(RefreshToken.builder().deviceId(deviceId).refreshToken(token).tokenExpirationTime(refreshTokenExpirationTime).build());
	}

	public boolean isValidRefreshToken(String deviceId , String requestRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findById(requestRefreshToken).orElseThrow(()->new IllegalArgumentException("Invalid RefreshToken"));
		if(refreshToken.getDeviceId().equals(deviceId)) {
			return true;
		}else {
			return false;
		}
	}
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteById(token);
	}
}
