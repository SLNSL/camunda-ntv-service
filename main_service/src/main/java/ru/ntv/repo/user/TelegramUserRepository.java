package ru.ntv.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.Role;
import ru.ntv.entity.users.TelegramUser;
import ru.ntv.entity.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {


    Optional<TelegramUser> findByUserId(Integer userId);
}