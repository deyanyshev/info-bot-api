package ru.it.vs.info_bot_api.service.report;

import ru.it.vs.info_bot_api.model.dto.ReportDto;
import ru.it.vs.info_bot_api.model.request.ReportSaveRequest;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    UUID create(ReportSaveRequest request);
    void approveReportById(UUID id);
    List<ReportDto> getAllReports();
    List<ReportDto> getAllReportsByUserId(UUID userId);
    ReportDto getReportById(UUID id);

}
