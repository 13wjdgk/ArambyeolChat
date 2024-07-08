package Arambyeol.chat.global.exception;

import Arambyeol.chat.global.dto.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return new ErrorResponse(errorCode.getStatusValue(),errorCode.getMessage());
    }
}
