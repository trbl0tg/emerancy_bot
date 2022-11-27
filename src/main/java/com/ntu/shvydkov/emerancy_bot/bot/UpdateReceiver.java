package com.ntu.shvydkov.emerancy_bot.bot;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.conditions.BotConditionUserContext;
import com.ntu.shvydkov.emerancy_bot.handler.BotConditionHandler;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

/**
 * Main {@link Update} handler. Defines the type of {@link Update} and passes it to other handlers.
 */
@Slf4j
@Component
public class UpdateReceiver {

    private final BotConditionHandler botConditionHandler;

    private final BotConditionUserContext botConditionUserContext;

    private final ReplyMessageService replyMessageService;

    public UpdateReceiver(BotConditionHandler botConditionHandler,
                          BotConditionUserContext botConditionUserContext,
                          ReplyMessageService replyMessageService) {
        this.botConditionHandler = botConditionHandler;
        this.botConditionUserContext = botConditionUserContext;
        this.replyMessageService = replyMessageService;
    }

    /**
     * Distributes incoming {@link Update} by its type and returns prepared response to user from specific handlers to main executable method.
     */
    public PartialBotApiMethod<? extends Serializable> handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            BotCondition botCondition = getBotCondition(message);
            log.info(
                    "Message from: {}; " +
                            "chat id: {};  " +
                            "text: {}; " +
                            "bot condition: {}",
                    message.getFrom().getUserName(),
                    message.getChatId(),
                    message.getText(),
                    botCondition
            );

            return botConditionHandler.handleTextMessageByCondition(message, botCondition);
        } else {
            log.error(
                    "Unsupported request from: {}; " +
                            "chatId: {}",
                    update.getMessage().getFrom().getUserName(),
                    update.getMessage().getChatId()
            );

            return replyMessageService.getTextMessage(update.getMessage().getChatId(), "Я могу принимать только текстовые сообщения!");
        }
    }

    /**
     * Defines current bot condition by user message to handle updates further in specific handlers.
     */
    private BotCondition getBotCondition(Message message) {
        Integer userId = message.getFrom().getId();
        String userTextMessage = message.getText();
        BotCondition botCondition;

        switch (userTextMessage) {
            case "/start":
            case "Головне меню":
                botCondition = BotCondition.MAIN_MENU;
                break;
            case "Допомога":
                botCondition = BotCondition.HELP;
                break;
//            case "Довідник":
//                botCondition = BotCondition.DICTIONARY;
//                break;
//            case "Розклад":
//                botCondition = BotCondition.SCHEDULE;
//                break;
//            case "Classroom":
//                botCondition = BotCondition.CLASSROOM;
//                break;
            default:
                botCondition = botConditionUserContext.getCurrentBotConditionForUserById(userId);
        }
        botConditionUserContext.setCurrentBotConditionForUserWithId(userId, botCondition);
        return botCondition;
    }
}
