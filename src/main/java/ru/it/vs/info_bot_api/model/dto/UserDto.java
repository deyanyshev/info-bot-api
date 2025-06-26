package ru.it.vs.info_bot_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String username;
    private Long chatId;
    private String phone;
    private boolean isAdmin;

}
