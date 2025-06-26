package ru.it.vs.info_bot_api.service.user;

import ru.it.vs.info_bot_api.model.dto.UserDto;
import ru.it.vs.info_bot_api.model.request.UserSaveRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void create(UserSaveRequest request);
    Optional<UserDto> getUserById(UUID id);
    Optional<UserDto> getUserByChatId(Long chatId);
    List<UserDto> getAllUsers();

}
