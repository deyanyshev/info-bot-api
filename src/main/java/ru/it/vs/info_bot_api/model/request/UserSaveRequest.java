package ru.it.vs.info_bot_api.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSaveRequest {

    private String username;
    private Long chatId;
    private String phone;

}
