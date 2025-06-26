package ru.it.vs.info_bot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.it.vs.info_bot_api.model.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findOneByChatId(Long id);

}
