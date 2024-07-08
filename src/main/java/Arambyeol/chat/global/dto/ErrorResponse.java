package Arambyeol.chat.global.dto;

public record ErrorResponse(boolean success, int errorCode, String message) {
    public ErrorResponse(int errorCode, String message) {
        this(false,errorCode, message);
    }
}
