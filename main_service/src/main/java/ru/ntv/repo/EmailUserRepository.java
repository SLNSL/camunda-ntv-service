package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.entity.users.EmailUser;

public interface EmailUserRepository extends JpaRepository<EmailUser, Integer> {
    void deleteAllByUserId(int id);
    EmailUser findByUserId(Integer userId);

}