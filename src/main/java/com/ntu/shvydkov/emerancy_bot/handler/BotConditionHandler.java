package com.ntu.shvydkov.emerancy_bot.handler;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.handler.message.MessageHandler;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Component
public class BotConditionHandler {

    private final List<MessageHandler> messageHandlers;
    private final ReplyMessageService replyMessageService;

    public BotConditionHandler(List<MessageHandler> messageHandlers, ReplyMessageService replyMessageService) {
        this.messageHandlers = messageHandlers;
        this.replyMessageService = replyMessageService;
    }

    public BotApiMethod<Message> handleTextMessageByCondition(Message message, BotCondition botCondition) {
        MessageHandler messageHandler;
        try {
            messageHandler = messageHandlers.stream()
                    .filter(m -> m.canHandle(botCondition))
                    .findAny()
                    .orElseThrow(NoHandlerFoundException::new);
        } catch (NoHandlerFoundException e) {
            log.error("No handler was found for current bot condition: {}", botCondition);
            return replyMessageService.getTextMessage(message.getChatId(), "Неможливо опрацювати запит.");
        }

        return messageHandler.handle(message);
    }

}

