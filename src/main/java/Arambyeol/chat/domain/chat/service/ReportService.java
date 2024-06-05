package Arambyeol.chat.domain.chat.service;

import org.springframework.stereotype.Service;

import Arambyeol.chat.domain.chat.dto.ReportChat;
import Arambyeol.chat.domain.chat.entity.Report;
import Arambyeol.chat.domain.chat.repository.ReportRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {
	private final ReportRepository reportRepository;
	public Report postReport(ReportChat report){
		return reportRepository.save(Report.builder().reportChat(report).build());
	}
}
