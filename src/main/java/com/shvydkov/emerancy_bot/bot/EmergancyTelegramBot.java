package com.shvydkov.emerancy_bot.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
public class EmergancyTelegramBot extends TelegramLongPollingBot {

    @Getter
//    @Value("${bot.username}")
    String botUsername = "ntu_emergancy_bot";

    @Getter
//    @Value("${bot.token}")
    String botToken = "5818457683:AAFmTcqYrT9sOxdBnndSeml6DOFpyfpIlmA";

//    private final UpdateReceiver updateReceiver;

//    public EmergancyTelegramBot(UpdateReceiver updateReceiver) {
//        this.updateReceiver = updateReceiver;
//    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("ass");
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}