package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.domain.DangerLevel;
import com.ntu.shvydkov.emerancy_bot.domain.Report;
import com.ntu.shvydkov.emerancy_bot.repo.ReportRepo;
import com.ntu.shvydkov.emerancy_bot.service.ReportService;
import com.ntu.shvydkov.emerancy_bot.service.UserRepoService;
import com.ntu.shvydkov.emerancy_bot.telegram.ReplyMessageService;
import com.ntu.shvydkov.emerancy_bot.telegram.keyboard.ReplyKeyboardMarkupBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReportMessageHandler implements MessageHandler {

    private volatile int state = 0;
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

    @Override
    public BotApiMethod<Message> handle(Message message) {
        Long chatId = message.getChatId();
        ReplyKeyboardMarkupBuilder result = ReplyKeyboardMarkupBuilder.create(chatId);

        if (state == 0) {
            if (message.getText().equals("Мої публікації")) {
                List<Report> allReportsByUserName = reportService.findAllReportsByUserName(message.getFrom().getUserName());
                if (allReportsByUserName == null || allReportsByUserName.isEmpty()) {
                    setDialogQuestionWithReturnToHome("Поки що ви нічого не публікували.", result);
                } else {
                    Double lat = allReportsByUserName.get(0).getLocation().getLatitude();
                    Double lon = allReportsByUserName.get(0).getLocation().getLongitude();
                    SendLocation sendLocation = new SendLocation(chatId.toString(), lat, lon);
                    return sendLocation;
                }

//                String reports = allReportsByUserName.stream().map(Report::getDisplayed).collect(Collectors.joining("\n"));


//                SendLocation sendLocation = new SendLocation(chatId, );
//                return replyMessageService.getTextMessage(chatId, "text");

//                result.setText(reports)
//                        .row()
//                        .button("Головне меню")
//                        .endRow();
//                return result.build();
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
            if (report.getTitle() != null && report.getDangerLevel() != null && report.getLocation() != null) {
                Report savedReport = reportRepo.save(report);
                report.setId(savedReport.getId());
                log.info("Report saved: " + report.toString());
            }
            result.setText("Запис опубліковано та відправлено на розгляд.")
                    .row()
                    .button("Мої публікації")
                    .endRow()
                    .row()
                    .button("Головне меню")
                    .endRow();
            state = 0;
        }

        return result.build();
    }

    private DangerLevel setDangerLevel(String message) {
        if (!Arrays.stream(DangerLevel.values()).map(Enum::name).collect(Collectors.toList()).contains(message)) {
            //return error message
            state = 0;
            return null;
        } else {
            return DangerLevel.valueOf(message);
        }
    }

    private ReplyKeyboardMarkupBuilder displayDangerLevels(ReplyKeyboardMarkupBuilder result) {
        for (DangerLevel level : DangerLevel.values()) {
            result.button(level.name());
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