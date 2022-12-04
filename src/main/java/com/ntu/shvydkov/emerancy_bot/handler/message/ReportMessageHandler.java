package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.domain.DangerLevel;
import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.handler.DangerLevelUtils;
import com.ntu.shvydkov.emerancy_bot.repo.ReportRepo;
import com.ntu.shvydkov.emerancy_bot.service.ReportService;
import com.ntu.shvydkov.emerancy_bot.service.UserRepoService;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import com.ntu.shvydkov.emerancy_bot.telegram.keyboard.ReplyKeyboardMarkupBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ntu.shvydkov.emerancy_bot.domain.DangerLevel.*;
import static com.ntu.shvydkov.emerancy_bot.handler.DangerLevelUtils.dangerLevelToText;
import static com.ntu.shvydkov.emerancy_bot.handler.DangerLevelUtils.textToDangerLevel;

@Slf4j
@Component
public class ReportMessageHandler implements MessageHandler {

    private volatile int state = 0;
    private volatile List<Report> responseReportsWithLocations = List.of();
    Report report = null;
    @Autowired
    private ReplyMessageService replyMessageService;
    @Autowired
    private ReportRepo reportRepo;
    @Autowired
    private ReportService reportService;
    @Autowired
    private UserRepoService userRepoService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.REPORT);
    }

    @SneakyThrows
    @Override
    public BotApiMethod<? extends Serializable> handle(Message message, AbsSender absSender) {
        Long chatId = message.getChatId();
        ReplyKeyboardMarkupBuilder result = ReplyKeyboardMarkupBuilder.create(chatId);

        if (state == 0) {
            if (message.getText().equals("Мої публікації")) {
                responseReportsWithLocations = reportService.findAllReportsByUserName(message.getFrom().getUserName());
                if (responseReportsWithLocations == null || responseReportsWithLocations.isEmpty()) {
                    return setDialogQuestionWithReturnToHome("Поки що ви нічого не публікували.", result).build();
                } else {

                    for (Report responseReportsWithLocation : responseReportsWithLocations) {
                        if (responseReportsWithLocation.getTextLocation() != null) {
                            absSender.execute(replyMessageService.getTextMessage(message.getChatId(), responseReportsWithLocation.getDisplayed()));
                        } else {
                            absSender.execute(replyMessageService.getTextMessage(message.getChatId(), responseReportsWithLocation.getDisplayed()));
                            absSender.execute(replyMessageService.getLocation(message.getChatId(), responseReportsWithLocation.getLocation()));
                        }
//                        absSender.execute(replyMessageService.getTextMessage(message.getChatId(), "\n"));
                    }
                    return result.build();
                }
            }

            report = new Report();
            report.setUser(userRepoService.createUserIfNotExistsOrReturnExisting(message));
            report.setCreated(LocalDateTime.now());
            setDialogQuestionWithReturnToHome("Опиши загрозу?", result);
            state = 1;
        } else if (state == 1) {
            report.setTitle(message.getText());
            result.setText("Опиши рівень загрози?")
                    .row();
            result = displayDangerLevels(result);
            result.endRow()
                    .row()
                    .button("Головне меню")
                    .endRow();
            state = 2;
        } else if (state == 2) {
            //location
            DangerLevel level = setDangerLevel(message.getText());
            if (level == null) {
                setDialogQuestionWithReturnToHome("Такого рівня загрози не передбачено.", result);
            } else {
                report.setDangerLevel(level);
                setDialogQuestionWithReturnToHome("Відправ геолокацію або напиши адресу та примітки словами в наступному повідомленні?", result);
                state = 3;
            }
        } else if (state == 3) {
            String location;
            if (message.hasLocation()) {
                Location location1 = message.getLocation();
                report.setLocation(new com.ntu.shvydkov.emerancy_bot.domain.Location(null, location1.getLatitude(), location1.getLongitude()));
            } else {
                location = message.getText();
                report.setTextLocation(location);
            }
            if (report.getTitle() != null && report.getDangerLevel() != null && (report.getLocation() != null || report.getTextLocation() != null)) {
                Report savedReport = reportRepo.save(report);
                report.setId(savedReport.getId());
                log.info("Report saved: " + report.toString());
                result.setText("Запис опубліковано та відправлено на розгляд.")
                        .row()
                        .button("Мої публікації")
                        .endRow()
                        .row()
                        .button("Головне меню")
                        .endRow();
            } else {
                log.error("Report is not saved: " + report.toString());
                result.setText("Помилка при створенні запису. Будь ласка зв`яжіться з розробником!")
                        .row()
                        .button("Мої публікації")
                        .endRow()
                        .row()
                        .button("Головне меню")
                        .endRow();
            }
            state = 0;
        }

        return result.build();
    }

    private DangerLevel setDangerLevel(String message) {
        if (!Arrays.stream(values()).map(DangerLevelUtils::dangerLevelToText).collect(Collectors.toList()).contains(message)) {
            state = 0;
            return null;
        } else {
            return textToDangerLevel(message);
        }
    }

    private ReplyKeyboardMarkupBuilder displayDangerLevels(ReplyKeyboardMarkupBuilder result) {
        for (DangerLevel level : values()) {
            result.button(dangerLevelToText(level));
        }
        return result;
    }

    private ReplyKeyboardMarkupBuilder setDialogQuestionWithReturnToHome(String question, ReplyKeyboardMarkupBuilder result) {
        return result.setText(question)
                .row()
                .button("Головне меню")
                .endRow();
    }
}