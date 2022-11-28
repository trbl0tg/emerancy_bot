package com.ntu.shvydkov.emerancy_bot.conditions;

public interface BotConditionObserved {

    BotCondition getCurrentBotConditionForUserById(Long userId);

    void setCurrentBotConditionForUserWithId(Long userId, BotCondition botCondition);

}
