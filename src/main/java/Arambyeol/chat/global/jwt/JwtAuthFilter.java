package Arambyeol.chat.global.jwt;

import java.io.IOException;
import java.util.List;

import Arambyeol.chat.global.dto.ErrorResponse;
import Arambyeol.chat.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private static final List<String> EXCLUDE_URLS = List.of("/login", "/signUp", "/ws-stomp","/error");
	private final JwtTokenUtil jwtTokenUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (EXCLUDE_URLS.contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = request.getHeader("Authorization");
		printRequestLog(request);
		JwtStatus status = jwtTokenUtil.isValidToken(token);
		if(!status.equals(JwtStatus.VALID)){
			ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
			if(status.equals(JwtStatus.EXPIRED)){
				errorCode = ErrorCode.EXPIRED_TOKEN;
			}
			response.setStatus(errorCode.getStatusValue());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = new ObjectMapper().writeValueAsString(new ErrorResponse(errorCode.getStatusValue(), errorCode.getMessage()));
			response.getWriter().write(json);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private static void printRequestLog(HttpServletRequest request) {
		log.info("Request JwtAuthFilter -- start");
		log.info("Request JwtAuthFilter -- RequestURI : ",request.getMethod()," / ", request.getRequestURI().toString());
		log.info("Request JwtAuthFilter -- Header : ", request.getHeader("Authorization").toString());
	}


}
