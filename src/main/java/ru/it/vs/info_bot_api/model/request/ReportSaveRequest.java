package ru.it.vs.info_bot_api.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReportSaveRequest {

    private String organisationName;
    private UUID userId;

}
