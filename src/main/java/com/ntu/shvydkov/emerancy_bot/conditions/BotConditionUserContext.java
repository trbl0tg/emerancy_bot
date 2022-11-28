package com.ntu.shvydkov.emerancy_bot.conditions;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotConditionUserContext implements BotConditionObserved {

    private final Map<Long, BotCondition> usersBotCondition = new HashMap<>();

    @Override
    public BotCondition getCurrentBotConditionForUserById(Long userId) {
        return usersBotCondition.getOrDefault(userId, BotCondition.MAIN_MENU);
    }

    @Override
    public void setCurrentBotConditionForUserWithId(Long userId, BotCondition botCondition) {
        usersBotCondition.put(userId, botCondition);
    }
}

