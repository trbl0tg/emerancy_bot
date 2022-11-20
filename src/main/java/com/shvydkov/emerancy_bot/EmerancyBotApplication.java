package com.shvydkov.emerancy_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class EmerancyBotApplication {

//    @Bean
//    FileReader fileReader() {
//        return new FileReader();
//    }

    public static void main(String[] args) {
        System.out.println("Bot starting...");
        ApiContextInitializer.init();
        SpringApplication.run(EmerancyBotApplication.class, args);
        System.out.println("Bot started!");
    }

}
