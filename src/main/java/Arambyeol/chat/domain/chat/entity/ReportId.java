package Arambyeol.chat.domain.chat.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class ReportId implements Serializable {
	@Column(name = "reporter_did", length = 50)
	private String reporterDid;

	@Column(name = "chat_id", length = 50)
	private String chatId;

	public ReportId(String reporterDid,String chatId) {
		this.reporterDid = reporterDid;
		this.chatId = chatId;

	}

}
