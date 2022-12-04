package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class HelpMessageHandler implements MessageHandler {

    private final ReplyMessageService replyMessageService;

    public HelpMessageHandler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message, AbsSender absSender) {
        Long chatId = message.getChatId();
        return replyMessageService.getTextMessage(chatId,
                "Щоб зкористатися ботом просто натискайте кнопки внизу екрану.\n" +
                        "При виникненні труднощей, або проблем з роботою боту, можете звернутися до розробника: @trb_dev.\n" +
                        "GIT: https://github.com/trbl0tg/emergancy_bot"
                        + "\n\n NTU 2022, Kyiv");
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.HELP);
    }
}