package ru.ntv.mail_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.mail_service.entity.EmailUser;

public interface EmailUserRepository extends JpaRepository<EmailUser, Integer> {
    EmailUser findByUserId(Integer userId);
}