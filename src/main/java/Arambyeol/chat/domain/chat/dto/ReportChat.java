package Arambyeol.chat.domain.chat.dto;

import org.springframework.validation.annotation.Validated;

import Arambyeol.chat.domain.chat.enums.ReportContent;
import Arambyeol.chat.domain.chat.valid.ValidReportContent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportChat(
	@NotBlank(message = "reporter_did는 필수입니다.")@Max(value = 50 , message = "reporter_did는 50자 이하여야 합니다.") String reporterDid,
	@NotBlank(message = "chat_id는 필수입니다.")@Max(value = 50 , message = "chat_id는 50자 이하여야 합니다.") String chatId,
	@NotNull(message = "content는 필수입니다.")@ValidReportContent(message = "유효하지 않은 신고 내용 유형입니다. 허용된 값: SEXUAL, VIOLENT, HATEFUL, HARMFUL, SPAM")ReportContent content) {
}
