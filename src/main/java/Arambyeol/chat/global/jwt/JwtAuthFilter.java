package Arambyeol.chat.global.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtTokenUtil jwtTokenUtil;
	private static final List<String> EXCLUDE_URLS = List.of("/generateAccessToken", "/login");
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (EXCLUDE_URLS.contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = request.getHeader("Authorization");
		log.info("Request JwtAuthFilter -- start");
		log.info("Request JwtAuthFilter -- RequestURI : ",request.getRequestURI());
		log.info("Request JwtAuthFilter -- Header : ",request.getHeader("Authorization"));
		System.out.println(request.getHeader("Authorization"));
		if(token != null && token.startsWith("Bearer")) {
			log.info("Request JwtAuthFilter -- token : ",token);
			//TODO 토큰 검증
			String accessToken = token.substring(7);
			if(jwtTokenUtil.validateToken(accessToken)) {
				String deviceId = jwtTokenUtil.getDeviceId(accessToken);
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
		filterChain.doFilter(request, response);
	}
}
