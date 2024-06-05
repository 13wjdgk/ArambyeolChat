package Arambyeol.chat.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Arambyeol.chat.domain.chat.entity.Report;
import Arambyeol.chat.domain.chat.entity.ReportId;

public interface ReportRepository extends JpaRepository<Report, ReportId> {
}
