package com.ntu.shvydkov.emerancy_bot.service;

import com.ntu.shvydkov.emerancy_bot.domain.TUserData;
import com.ntu.shvydkov.emerancy_bot.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@Service
public class UserRepoService {
    @Autowired
    private UsersRepo repo;

    public Boolean checkIsUserExists(Message message) {
        return repo.existsByUsername(message.getFrom().getUserName());
    }

    public TUserData createUserIfNotExistsOrReturnExisting(Message message) {
        User from = message.getFrom();
        if (checkIsUserExists(message)) {
            return repo.findByUsername(from.getUserName());
        } else {
            TUserData userToSave = new TUserData(from.getUserName(), LocalDateTime.now());
            return repo.save(userToSave);
        }
    }

    public TUserData findUserByUsername(String username){
        return repo.findByUsername(username);
    }
}
