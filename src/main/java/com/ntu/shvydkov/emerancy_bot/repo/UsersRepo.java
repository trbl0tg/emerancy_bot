package com.ntu.shvydkov.emerancy_bot.repo;

import com.ntu.shvydkov.emerancy_bot.domain.TUserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepo extends CrudRepository<TUserData, UUID> {
    Boolean existsByUsername(String username);
    TUserData findByUsername(String username);
}
