package Arambyeol.chat.global.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	private final CustomUserDetailsService customUserDetailsService;

	public JwtTokenUtil(@Value("${jwt.secret}") String key, @Value("${jwt.AccessExpire}") long accessTokenExpTime,@Value("${jwt.RefreshExpire}") long refreshTokenExpTime,
		CustomUserDetailsService customUserDetailsService) {
		this.key = Keys.hmacShaKeyFor(key.getBytes());
		this.accessTokenExpTime = accessTokenExpTime*1000;
		this.refreshTokenExpTime = refreshTokenExpTime*1000;
		this.customUserDetailsService = customUserDetailsService;
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
	public void isValidToken(String token) {
		if(token != null && token.startsWith("Bearer")) {
			log.info("Request JwtAuthFilter -- token : ", token);
			//TODO 토큰 검증
			String accessToken = token.substring(7);
			if(validateToken(accessToken)) {
				String deviceId = getDeviceId(accessToken);
				//토큰에서 정보 검증 및 UserDetailsService를 통해 정보 가져오기
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(deviceId);
				if(userDetails != null) {
					//인증 토큰 생성
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					//SecurityContextHolder에 인증 정보 저장
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}else{
			log.info("Request JwtAuthFilter -- token is invalid");
		}
	}
}
