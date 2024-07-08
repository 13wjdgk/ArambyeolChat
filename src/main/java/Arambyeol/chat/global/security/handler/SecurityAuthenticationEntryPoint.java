package Arambyeol.chat.global.security.handler;

import Arambyeol.chat.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("인증되지 않은 요청 발생 : {}", authException.getMessage());
        log.error("요청 URL : {}", request.getRequestURI());

        int status = HttpStatus.UNAUTHORIZED.value();
        ErrorResponse errorResponse = new ErrorResponse(status,"인증되지 않았습니다.");
        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
