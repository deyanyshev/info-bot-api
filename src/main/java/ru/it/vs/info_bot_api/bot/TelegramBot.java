package ru.it.vs.info_bot_api.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.it.vs.info_bot_api.bot.handler.CommandHandler;
import ru.it.vs.info_bot_api.bot.handler.ReportHandler;
import ru.it.vs.info_bot_api.config.BotConfig;
import ru.it.vs.info_bot_api.enums.BotState;

import java.util.Map;

import static ru.it.vs.info_bot_api.enums.BotState.START;
import static ru.it.vs.info_bot_api.utils.constants.BotConstants.CHAT_STATES_MAP;

@Component
public class TelegramBot extends AbilityBot {

    public final Map<Long, BotState> chatStates;
    public final BotConfig config;

    private final CommandHandler commandHandler;
    private final ReportHandler reportHandler;

    @Autowired
    private TelegramBot(@Lazy CommandHandler commandHandler, @Lazy ReportHandler reportHandler, BotConfig config) {
        super(config.getToken(), config.getName());
        this.config = config;

        chatStates = db.getMap(CHAT_STATES_MAP);

        this.commandHandler = commandHandler;
        this.reportHandler = reportHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            chatStates.putIfAbsent(chatId, START);

            if (message.hasText() && message.getText().startsWith("/")) {
                commandHandler.handle(message);
                return;
            }

            switch (chatStates.get(chatId).getHandlerType()) {
                case COMMAND_HANDLER -> commandHandler.handle(message);
                case REPORT_HANDLER -> reportHandler.handle(message);
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            chatStates.putIfAbsent(chatId, START);

            switch (chatStates.get(chatId).getHandlerType()) {
                case COMMAND_HANDLER -> commandHandler.handleCallback(update.getCallbackQuery());
                case REPORT_HANDLER -> reportHandler.handleCallback(update.getCallbackQuery());
            }
        }
    }

    @Override
    public long creatorId() {
        return 1L;
    }

}
