package ru.it.vs.info_bot_api.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.it.vs.info_bot_api.exceptions.ApiException;
import ru.it.vs.info_bot_api.mapper.ReportMapper;
import ru.it.vs.info_bot_api.model.dto.ReportDto;
import ru.it.vs.info_bot_api.model.entity.Report;
import ru.it.vs.info_bot_api.model.entity.User;
import ru.it.vs.info_bot_api.model.request.ReportSaveRequest;
import ru.it.vs.info_bot_api.repository.ReportRepo;
import ru.it.vs.info_bot_api.repository.UserRepo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserRepo userRepo;
    private final ReportRepo reportRepo;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public UUID create(ReportSaveRequest request) {
        Report report = prepareEntity(request);
        return reportRepo.save(report).getId();
    }

    @Override
    @Transactional
    public void approveReportById(UUID id) {
        Report report = reportRepo.findById(id).orElseThrow(
                () -> ApiException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Report was not found with id: " + id)
                        .build()
        );

        report.setApproved(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> getAllReports() {
        return reportRepo.findAll().stream()
                .map(reportMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> getAllReportsByUserId(UUID userId) {
        return reportRepo.findAllByUserId(userId).stream()
                .map(reportMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDto getReportById(UUID id) {
        return reportRepo.findById(id).map(reportMapper::toDto).orElseThrow(
                () -> ApiException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Report was not found with id: " + id)
                        .build()
        );
    }

    private Report prepareEntity(ReportSaveRequest request) {
        Report report = new Report();

        report.setOrganisationName(request.getOrganisationName());
        report.setApproved(false);

        User user = userRepo.findById(request.getUserId()).orElseThrow(
                () -> ApiException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("User not found with id: " + request.getUserId())
                        .build()
        );

        report.setUser(user);
        return report;
    }
}
