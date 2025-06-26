package ru.it.vs.info_bot_api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.it.vs.info_bot_api.mapper.UserMapper;
import ru.it.vs.info_bot_api.model.dto.UserDto;
import ru.it.vs.info_bot_api.model.entity.User;
import ru.it.vs.info_bot_api.model.request.UserSaveRequest;
import ru.it.vs.info_bot_api.repository.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void create(UserSaveRequest request) {
        User user = prepareEntity(request);
        userRepo.save(user);
    }

    @Override
    public Optional<UserDto> getUserById(UUID id) {
        return userRepo.findById(id).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByChatId(Long chatId) {
        return userRepo.findOneByChatId(chatId).map(userMapper::toDto);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    private User prepareEntity(UserSaveRequest request) {
        User user = new User();

        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setChatId(request.getChatId());
        user.setAdmin(false);

        return user;
    }

}
