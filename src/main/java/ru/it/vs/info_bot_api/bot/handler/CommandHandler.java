package ru.it.vs.info_bot_api.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.it.vs.info_bot_api.model.dto.ReportDto;
import ru.it.vs.info_bot_api.model.dto.UserDto;
import ru.it.vs.info_bot_api.model.request.UserSaveRequest;
import ru.it.vs.info_bot_api.service.report.ReportService;
import ru.it.vs.info_bot_api.service.user.UserService;
import ru.it.vs.info_bot_api.utils.Utility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.it.vs.info_bot_api.bot.KeyboardButtons.*;
import static ru.it.vs.info_bot_api.enums.BotState.*;
import static ru.it.vs.info_bot_api.utils.constants.BotCommands.*;
import static ru.it.vs.info_bot_api.utils.constants.BotConstants.*;

@Component
public class CommandHandler extends BaseHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;

    @Override
    public void handle(Message message) {
        Optional<UserDto> user = userService.getUserByChatId(message.getChatId());

        if (message.hasText() && message.getText().startsWith("/") && user.isPresent()) {
            switch (message.getText()) {
                case START_COMMAND -> start(message.getChatId());
                case NEW_REPORT_COMMAND -> startReport(message.getChatId());
                case REPORTS_COMMAND -> sendReports(message.getChatId());
                case MY_REPORTS_COMMAND -> sendReportsByChatId(message.getChatId());
            }
        } else {
            switch (bot.chatStates.get(message.getChatId())) {
                case START -> start(message.getChatId());
                case CONTACT -> contact(message.getChatId(), message.getContact());
            }
        }
    }

    @Override
    public void handleCallback(CallbackQuery callbackQuery) {
    }

    private void start(Long chatId) {
        if (userService.getUserByChatId(chatId).isEmpty()) {
            sendMessage(chatId, CONTACT_MESSAGE, keyboardMarkup(List.of(CONTACT_BUTTON)));
            bot.chatStates.put(chatId, CONTACT);
        } else {
           help(chatId);
        }
    }

    private void contact(Long chatId, Contact contact) {
        String username = contact.getFirstName() + (contact.getLastName() != null ? " " + contact.getLastName() : "");

        userService.create(UserSaveRequest.builder()
                .username(username)
                .chatId(chatId)
                .phone(contact.getPhoneNumber())
                .build());

        help(chatId);
    }

    private void help(Long chatId) {
        UserDto user = userService.getUserByChatId(chatId).orElseThrow();
        sendMessage(chatId, String.format(HELP_MESSAGE, user.getUsername()));
    }

    private void startReport(Long chatId) {
        sendMessage(chatId, START_REPORT_MESSAGE, keyboardMarkup(List.of(LOCATION_BUTTON)));
        bot.chatStates.put(chatId, ADD_LOCATION);
    }

    private void sendReports(Long chatId) {
        List<UserDto> users = userService.getAllUsers();
        UserDto user = users.stream()
                .filter(item -> Objects.equals(item.getChatId(), chatId))
                .findFirst()
                .orElseThrow();

        if (user.isAdmin()) {
            sendMessage(chatId, USER_KEYBOARD_MESSAGE, userKeyboardMarkup(users));
            bot.chatStates.put(chatId, REPORTS);
        } else {
            sendMessage(chatId, "Необходимо быть администратором.");
        }
    }

    private void sendReportsByChatId(Long chatId) {
        UserDto user = userService.getUserByChatId(chatId).orElseThrow();
        List<ReportDto> reports = reportService.getAllReportsByUserId(user.getId());

        sendMessage(chatId, Utility.getReportMessage(reports));
        bot.chatStates.put(chatId, START);
    }

}
