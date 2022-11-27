package com.ntu.shvydkov.emerancy_bot.conditions;

public interface BotConditionObserved {

    BotCondition getCurrentBotConditionForUserById(Integer userId);

    void setCurrentBotConditionForUserWithId(Integer userId, BotCondition botCondition);

}
