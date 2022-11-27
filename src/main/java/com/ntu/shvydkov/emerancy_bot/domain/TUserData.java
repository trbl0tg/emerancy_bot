package com.ntu.shvydkov.emerancy_bot.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "users")
public class TUserData {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String username;
    private LocalDateTime created;
    private StateType state;

    public TUserData(String username, LocalDateTime created, StateType state) {
        this.username = username;
        this.created = created;
        this.state = state;
    }
}
