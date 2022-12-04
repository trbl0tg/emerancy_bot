package com.ntu.shvydkov.emerancy_bot.handler;

import com.ntu.shvydkov.emerancy_bot.domain.DangerLevel;

import static com.ntu.shvydkov.emerancy_bot.domain.DangerLevel.*;
import static com.ntu.shvydkov.emerancy_bot.domain.DangerLevel.MEDIUM;

public class DangerLevelUtils {
    public static String dangerLevelToText(DangerLevel dangerLevel) {
        switch (dangerLevel) {
            case INFO:
                return "Інформація";
            case LOW:
                return "Низький";
            case MEDIUM:
                return "Середній";
            case HIGH:
                return "Високий";
            case EXTREME:
                return "Максимальний";
        }
        return "Error!";
    }

    public static DangerLevel textToDangerLevel(String dangerLevel) {
        switch (dangerLevel) {
            case "Інформація":
                return INFO;
            case "Низький":
                return LOW;
            case "Середній":
                return MEDIUM;
            case "Високий":
                return HIGH;
            case "Максимальний":
                return EXTREME;
        }
        return MEDIUM;
    }
}
