package ru.it.vs.info_bot_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.it.vs.info_bot_api.model.dto.ReportDto;
import ru.it.vs.info_bot_api.model.entity.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "isApproved", source = "approved")
    ReportDto toDto(Report entity);

}
