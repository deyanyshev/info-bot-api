package ru.it.vs.info_bot_api.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.it.vs.info_bot_api.model.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.it.vs.info_bot_api.utils.constants.ButtonConstants.*;

public class KeyboardButtons {

    public static final KeyboardButton CONTACT_BUTTON = KeyboardButton.builder()
            .text(CONTACT_BUTTON_TEXT)
            .requestContact(true)
            .build();
    public static final KeyboardButton LOCATION_BUTTON = KeyboardButton.builder()
            .text(LOCATION_BUTTON_TEXT)
            .requestLocation(true)
            .build();
    public static final KeyboardButton SEND_BUTTON = KeyboardButton.builder()
            .text(SEND_BUTTON_TEXT)
            .build();

    public static ReplyKeyboardMarkup keyboardMarkup(List<KeyboardButton> buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                buttons.stream().map(button -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(button);
                    return row;
                }).toList()
        );
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup inlineKeyboardMarkup(List<InlineKeyboardButton> buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons.stream().map(List::of).toList());

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup userKeyboardMarkup(List<UserDto> users) {
        return InlineKeyboardMarkup.builder()
                .keyboard(new ArrayList<>(users.stream().map(
                        user -> List.of(InlineKeyboardButton.builder()
                                .text(String.format("%s (%s)", user.getUsername(), user.getPhone()))
                                .callbackData(String.valueOf(user.getId()))
                                .build()
                        )).toList()
                ))
                .build();
    }

    public static InlineKeyboardButton approveButton(UUID reportId) {
        return  InlineKeyboardButton.builder()
                .text(APPROVE_BUTTON_TEXT)
                .callbackData(String.valueOf(reportId))
                .build();
    }

}
