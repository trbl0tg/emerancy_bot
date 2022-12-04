package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.repo.UsersRepo;
import com.ntu.shvydkov.emerancy_bot.service.UserRepoService;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import com.ntu.shvydkov.emerancy_bot.telegram.keyboard.ReplyKeyboardMarkupBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Handles {@link Message} when {@link BotCondition} is {@link BotCondition#MAIN_MENU}.
 * <p>
 * Sends reply keyboard with main menu to interact with it.
 */
@Component
public class StartMessageHandler implements MessageHandler {

    private final ReplyMessageService replyMessageService;
    private final UsersRepo usersRepo;
    private final UserRepoService userRepoService;

    public StartMessageHandler(ReplyMessageService replyMessageService, UsersRepo userRepo, UserRepoService userRepoService) {
        this.replyMessageService = replyMessageService;
        this.usersRepo = userRepo;
        this.userRepoService = userRepoService;
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.MAIN_MENU);
    }

    @Override
    public SendMessage handle(Message message, AbsSender absSender) {
        userRepoService.createUserIfNotExistsOrReturnExisting(message);
        return getMainMenu(message.getChatId());
    }

    private SendMessage getMainMenu(Long chatId) {
        return ReplyKeyboardMarkupBuilder.create(chatId)
                .setText("Привіт! "
                        + "\n\nЩоб почати користуватися функціоналом, натискай на потрібну кнопку внизу. ")
                .row()
                .button("Сповістити про загрозу")
                .endRow()
                .row()
                .button("Мої публікації")
                .endRow()
                .row()
                .button("Довідник")
                .button("Допомога")
                .endRow()
                .build();
    }

}