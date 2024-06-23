package Arambyeol.chat.global.security.handler;

import Arambyeol.chat.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("접근 권한이 없는 요청 발생 : {}", accessDeniedException.getMessage());
        log.error("요청 URL : {}", request.getRequestURI());

        int status = HttpStatus.FORBIDDEN.value();
        ErrorResponse errorResponse = new ErrorResponse(status,"접근 권한이 없습니다.");
        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}

