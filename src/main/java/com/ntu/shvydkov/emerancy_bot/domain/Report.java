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

    public String getDisplayed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "Назва : " + this.title + "\n" +
                "Рівень загрози : " + this.getDangerLevel() + "\n" +
                "Дата : " + this.getCreated().format(formatter) + "\n";
        if (this.textLocation != null || !this.textLocation.isEmpty()) {
            s += "Локація : " + this.textLocation + "\n";
        } else {
            s += "Локація : ";
        }
        return s;
    }
}
