package ru.it.vs.info_bot_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ru.it.vs.info_bot_api.enums.HandlerType.COMMAND_HANDLER;
import static ru.it.vs.info_bot_api.enums.HandlerType.REPORT_HANDLER;

@Getter
@AllArgsConstructor
public enum BotState {

    START(COMMAND_HANDLER),
    CONTACT(COMMAND_HANDLER),
    REPORTS(REPORT_HANDLER),
    ADD_LOCATION(REPORT_HANDLER),
    ADD_ORGANISATION(REPORT_HANDLER),
    ADD_PHOTO(REPORT_HANDLER);

    private final HandlerType handlerType;
}
