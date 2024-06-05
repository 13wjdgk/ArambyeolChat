package Arambyeol.chat.domain.chat.entity;

import Arambyeol.chat.domain.chat.dto.ReportChat;
import Arambyeol.chat.domain.chat.enums.ReportContent;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Report {
	@EmbeddedId
	private ReportId reportId;
	private ReportContent content;

	@Builder
	public Report(ReportChat reportChat){
		this.reportId = new ReportId(reportChat.reporterDid(),reportChat.chatId());
		this.content = reportChat.content();
	}
}
