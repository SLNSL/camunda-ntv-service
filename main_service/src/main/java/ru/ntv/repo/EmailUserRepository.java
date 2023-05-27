package ru.ntv.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.entity.users.EmailUser;

public interface EmailUserRepository extends JpaRepository<EmailUser, Integer> {
    void deleteAllByUserId(int id);
}