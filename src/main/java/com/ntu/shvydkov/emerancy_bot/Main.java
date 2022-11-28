package com.ntu.shvydkov.emerancy_bot;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Main {

//    @Bean
//    FileReader fileReader() {
//        return new FileReader();
//    }

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Bot starting...");
        new TelegramBotsApi(DefaultBotSession.class);
        SpringApplication.run(Main.class, args);
        System.out.println("Bot started!");
    }

}
