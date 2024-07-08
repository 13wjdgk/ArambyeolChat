package Arambyeol.chat.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED , "존재하지 않는 Refresh 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED , "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN , "유효하지 않는 토큰입니다.");
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public int getStatusValue() {
        return status.value();
    }
}
