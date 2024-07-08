package Arambyeol.chat.domain.auth.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;

@RedisHash(value = "refreshToken"  )
public class RefreshToken {
	@Id
	private String refreshToken;

	@Getter
	private String deviceId;

	@TimeToLive
	private long tokenExpirationTime;

	@Builder
	public RefreshToken( String deviceId,String refreshToken, long tokenExpirationTime) {
		this.deviceId = deviceId;
		this.refreshToken = refreshToken;
		this.tokenExpirationTime = tokenExpirationTime;
	}


}
