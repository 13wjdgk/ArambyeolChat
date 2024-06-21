package Arambyeol.chat.global.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
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

	private static final List<String> EXCLUDE_URLS = List.of("/login", "/signUp", "/ws-stomp");
	private final JwtTokenUtil jwtTokenUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (EXCLUDE_URLS.contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = request.getHeader("Authorization");
		printRequestLog(request);
		jwtTokenUtil.isValidToken(token);
		filterChain.doFilter(request, response);
	}

	private static void printRequestLog(HttpServletRequest request) {
		log.info("Request JwtAuthFilter -- start");
		log.info("Request JwtAuthFilter -- RequestURI : ",request.getMethod()," / ", request.getRequestURI().toString());
		log.info("Request JwtAuthFilter -- Header : ", request.getHeader("Authorization").toString());
	}


}
