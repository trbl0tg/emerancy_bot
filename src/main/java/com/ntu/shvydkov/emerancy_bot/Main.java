package com.ntu.shvydkov.emerancy_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class Main {

//    @Bean
//    FileReader fileReader() {
//        return new FileReader();
//    }

    public static void main(String[] args) {
        System.out.println("Bot starting...");
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
        System.out.println("Bot started!");
    }

}
