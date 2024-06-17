package Arambyeol.chat.global.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenUtil {

	private final Key key;
	private final long accessTokenExpTime;
	private final long refreshTokenExpTime;

	public JwtTokenUtil(@Value("${jwt.secret}") String key, @Value("${jwt.AccessExpire}") long accessTokenExpTime,@Value("${jwt.RefreshExpire}") long refreshTokenExpTime) {
		this.key = Keys.hmacShaKeyFor(key.getBytes());
		this.accessTokenExpTime = accessTokenExpTime*1000;
		this.refreshTokenExpTime = refreshTokenExpTime*1000;
	}

	public String createAccessToken(String deviceId) {
		return Jwts.builder()
				.setSubject(deviceId)
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	public String createRefreshToken(String deviceId) {
		return Jwts.builder()
			.setSubject(deviceId)
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpTime))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
	public String getDeviceId(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token",e);
			return false;
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			log.info("Expired JWT Token",e);
			return false;
		} catch (io.jsonwebtoken.UnsupportedJwtException e) {
			log.info("Unsupported JWT Token",e);
			return false;
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty",e);
			return false;
		}
	}
}
