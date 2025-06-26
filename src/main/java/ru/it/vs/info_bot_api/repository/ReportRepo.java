package ru.it.vs.info_bot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.it.vs.info_bot_api.model.entity.Report;

import java.util.List;
import java.util.UUID;

public interface ReportRepo extends JpaRepository<Report, UUID> {

    List<Report> findAllByUserId(UUID userId);

}
