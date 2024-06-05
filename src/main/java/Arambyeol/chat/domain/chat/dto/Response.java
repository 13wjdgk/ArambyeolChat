package Arambyeol.chat.domain.chat.dto;

import Arambyeol.chat.domain.chat.enums.Status;

/**
 * 공통 Response
 *
 * @param status 응답 상태
 * @param data 응답 데이터
 * @param message 응답 메세지
 *
 * */
public record Response(Status status, Object data, String message) {

}