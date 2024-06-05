package Arambyeol.chat.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record SuccessSingleResponse<T>(boolean success, int errorCode, String message, @JsonInclude(JsonInclude.Include.NON_NULL) T data) {
	public SuccessSingleResponse(String message, T data) {
		this(true, 0, message, data);
	}
}

