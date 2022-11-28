package com.ntu.shvydkov.emerancy_bot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private String location;
    @Enumerated
    private DangerLevel dangerLevel;
    @Enumerated
    private ReportState reportState = ReportState.DRAFT;
    private LocalDateTime created;
    @ManyToOne
    private TUserData user;

    public String getDisplayed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Назва : " + this.title + "\n" +
                "Локація : " + this.getLocation() + "\n" +
                "Рівень загрози : " + this.getDangerLevel() + "\n" +
                "Дата : " + this.getCreated().format(formatter) + "\n";
    }
}
