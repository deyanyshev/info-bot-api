package ru.it.vs.info_bot_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.it.vs.info_bot_api.model.dto.UserDto;
import ru.it.vs.info_bot_api.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "isAdmin", source = "admin")
    UserDto toDto(User entity);

}
