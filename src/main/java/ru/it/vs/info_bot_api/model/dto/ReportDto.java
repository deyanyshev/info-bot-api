package ru.it.vs.info_bot_api.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ReportDto {

    private UUID id;
    private String organisationName;
    private UserDto user;
    private boolean isApproved;

}
