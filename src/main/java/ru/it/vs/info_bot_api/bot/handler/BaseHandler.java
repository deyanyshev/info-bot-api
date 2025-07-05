package ru.it.vs.info_bot_api.bot.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.it.vs.info_bot_api.bot.TelegramBot;

import static ru.it.vs.info_bot_api.utils.Utility.formatMessageText;
import static ru.it.vs.info_bot_api.utils.constants.BotConstants.MARKDOWN_V2;

@Slf4j
public abstract class BaseHandler {

    @Autowired
    protected TelegramBot bot;

    public abstract void handle(Message message);
    public abstract void handleCallback(CallbackQuery callbackQuery);

    @SneakyThrows
    protected void sendMessage(long chatId, String text) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(formatMessageText(text))
                .parseMode(MARKDOWN_V2)
                .build()
        );
    }

    @SneakyThrows
    protected void sendMessage(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(formatMessageText(text))
                .replyMarkup(keyboardMarkup)
                .parseMode(MARKDOWN_V2)
                .build()
        );
    }

    protected void sendMessage(long chatId, String text, InlineKeyboardMarkup markup) {
        try {
            bot.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(formatMessageText(text))
                    .replyMarkup(markup)
                    .parseMode(MARKDOWN_V2)
                    .build()
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    protected void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup markup) {
        try {
            bot.execute(EditMessageText.builder()
                    .replyMarkup(markup)
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(text)
                    .parseMode(MARKDOWN_V2)
                    .build()
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    protected void forwardMessage(Long toChatId, Long fromChatId, Integer messageId) {
        try {
            bot.execute(ForwardMessage.builder()
                    .chatId(toChatId)
                    .fromChatId(fromChatId)
                    .messageId(messageId)
                    .build()
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
