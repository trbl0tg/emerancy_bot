package com.ntu.shvydkov.emerancy_bot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class TUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private LocalDateTime created;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports;
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
//    private Location locationl
//    private List<Subscriptions> subscriptions;

    public TUserData(String username, LocalDateTime created) {
        this.username = username;
        this.created = created;
    }

    @Override
    public String toString() {
        return "TUserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
