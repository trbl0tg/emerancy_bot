package com.ntu.shvydkov.emerancy_bot.domain;

import com.ntu.shvydkov.emerancy_bot.handler.DangerLevelUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    @Enumerated
    private DangerLevel dangerLevel;
    @Enumerated
    private ReportState reportState = ReportState.DRAFT;
    private LocalDateTime created;
    @ManyToOne
    private TUserData user;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Location location;
    private String textLocation;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhotoEntity> photos;

    public String getDisplayed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "Назва : " + this.title + "\n" +
                "Рівень загрози : " + DangerLevelUtils.dangerLevelToText(this.getDangerLevel()) + "\n" +
                "Дата : " + this.getCreated().format(formatter)  +
                "\nCтатус : " + getDisplayMessage(this.getReportState()) +
                "\n";
        if (this.textLocation != null) {
            s += "Локація : " + this.textLocation + "\n";
        } else {
            s += "Локація : ";
        }
        return s;
    }

    private String getDisplayMessage(ReportState reportState) {
        switch (reportState){
            case DRAFT:
                return "В черзі на розгляд.";
            case APPROVED:
                return "Корректність підтверджено.";
            case PROCESSED:
                return "Розглянуто.";
            case ACCEPTED_BY_AUTHORITY:
                return "Опрацьовано службами.";
            case REJECTED:
                return "Відмовлено у опрацюванні.";
        }
        return "Не відомий статус публікації!";
    }
}
