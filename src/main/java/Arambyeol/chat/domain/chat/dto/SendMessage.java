package Arambyeol.chat.domain.chat.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record SendMessage(String senderDid , String message,  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime sendTime) {
}
