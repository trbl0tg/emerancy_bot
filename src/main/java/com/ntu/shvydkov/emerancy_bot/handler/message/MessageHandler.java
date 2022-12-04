package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.Serializable;

public interface MessageHandler {

    boolean canHandle(BotCondition botCondition);

    BotApiMethod<? extends Serializable> handle(Message message, AbsSender absSender);

}
