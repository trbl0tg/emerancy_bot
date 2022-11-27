package com.ntu.shvydkov.emerancy_bot.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;


public interface TelegramUpdateExtractor {

    Long getChatId(Update update);

    String getUserName(Update update);

    Integer getUserId(Update update);

    Integer getMessageId(Update update);

    String getUserLanguage(Update update);

}
