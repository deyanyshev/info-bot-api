package ru.it.vs.info_bot_api.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.it.vs.info_bot_api.model.dto.ReportDto;
import ru.it.vs.info_bot_api.model.dto.UserDto;
import ru.it.vs.info_bot_api.model.request.ReportSaveRequest;
import ru.it.vs.info_bot_api.service.report.ReportService;
import ru.it.vs.info_bot_api.service.user.UserService;
import ru.it.vs.info_bot_api.utils.Utility;

import java.util.*;

import static ru.it.vs.info_bot_api.bot.KeyboardButtons.*;
import static ru.it.vs.info_bot_api.enums.BotState.*;
import static ru.it.vs.info_bot_api.utils.constants.BotConstants.*;
import static ru.it.vs.info_bot_api.utils.constants.ButtonConstants.APPROVE_BUTTON_TEXT;

@Component
public class ReportHandler extends BaseHandler {

    private final Map<Long, ReportSaveRequest> reports = new HashMap<>();
    private final Map<Long, List<Integer>> forwards = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;

    @Override
    public void handle(Message message) {
        switch (bot.chatStates.get(message.getChatId())) {
            case ADD_LOCATION -> addLocation(message.getChatId(), message.getMessageId());
            case ADD_ORGANISATION -> addOrganisation(message.getChatId(), message.getText());
            case ADD_PHOTO -> addPhoto(message);
        }
    }

    @Override
    public void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String buttonText = message.getReplyMarkup().getKeyboard().get(0).get(0).getText();

        if (Objects.equals(buttonText, APPROVE_BUTTON_TEXT)) {
            approveReport(message, callbackQuery.getData());
            return;
        }

        sendReportByUserId(message.getChatId(), callbackQuery.getData());
    }

    private void addLocation(Long chatId, Integer messageId) {
        UserDto user = userService.getUserByChatId(chatId).orElseThrow();
        reports.put(chatId, ReportSaveRequest.builder()
                .userId(user.getId())
                .build()
        );

        forwards.put(chatId, new ArrayList<>(List.of(messageId)));

        sendMessage(chatId, ADD_ORGANISATION_NAME_MESSAGE, keyboardMarkup(List.of()));
        bot.chatStates.put(chatId, ADD_ORGANISATION);
    }

    private void addOrganisation(Long chatId, String organisationName) {
        if (organisationName == null) return;

        ReportSaveRequest report = reports.get(chatId);
        report.setOrganisationName(organisationName);
        reports.put(chatId, report);

        sendMessage(chatId, ADD_PHOTO_MESSAGE, keyboardMarkup(List.of(SEND_BUTTON)));
        bot.chatStates.put(chatId, ADD_PHOTO);
    }

    private void addPhoto(Message message) {
        Long chatId = message.getChatId();

        if (message.hasText() && Objects.equals(message.getText(), SEND_BUTTON.getText())) {
            ReportSaveRequest request = reports.get(chatId);
            UUID reportId = reportService.create(request);

            UserDto user = userService.getUserByChatId(chatId).orElseThrow();
            List<UserDto> admins = userService.getAllUsers().stream()
                    .filter(UserDto::isAdmin)
                    .toList();

            for (UserDto admin : admins) {
                sendMessage(admin.getChatId(), String.format("*Отчет* от %s\n*Организация:* %s\n*Номер отправителя:* %s",
                        user.getUsername(), request.getOrganisationName(), user.getPhone()
                ), inlineKeyboardMarkup(List.of(approveButton(reportId))));

                for (Integer messageId : forwards.get(chatId)) {
                    forwardMessage(admin.getChatId(), chatId, messageId);
                }
            }

            sendMessage(chatId, String.format(FINISH_REPORT_MESSAGE, user.getUsername()));
            bot.chatStates.put(chatId, START);
        } else {
            forwards.get(chatId).add(message.getMessageId());
            sendMessage(chatId, "Добавлено " + (forwards.get(chatId).size() - 1), keyboardMarkup(List.of(SEND_BUTTON)));
        }
    }

    private void sendReportByUserId(Long chatId, String callback) {
        UserDto user = userService.getUserById(UUID.fromString(callback)).orElseThrow();
        List<ReportDto> reports = reportService.getAllReportsByUserId(user.getId());

        sendMessage(chatId, Utility.getReportMessage(reports));
        bot.chatStates.put(chatId, START);
    }

    private void approveReport(Message message, String callback) {
        UUID reportId = UUID.fromString(callback);
        reportService.approveReportById(reportId);

        ReportDto report = reportService.getReportById(reportId);
        sendMessage(message.getChatId(), "Отчёт одобрен!");
        sendMessage(report.getUser().getChatId(), String.format(
                "%s, твой отчёт был одобрен. Организация: %s",
                report.getUser().getUsername(), report.getOrganisationName()
        ));

        editMessage(message.getChatId(), message.getMessageId(), message.getText(), inlineKeyboardMarkup(List.of()));
    }

}
