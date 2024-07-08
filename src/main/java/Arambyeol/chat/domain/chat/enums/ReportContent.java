package Arambyeol.chat.domain.chat.enums;

public enum ReportContent {
	SEXUAL("성적인 내용"),
	VIOLENT ("폭력적 또는 혐오스러운 내용"),
	HATEFUL("증오 또는 학대하는 내용"),
	HARMFUL("유해하거나 위험한 내용"),
	SPAM ("스팸 또는 혼동을 야기하는 내용");

	private String content;
	ReportContent(String content){
		this.content = content;
	}
}