package ru.ntv.mail_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.mail_service.entity.EmailUserTheme;

public interface EmailUserThemeRepository extends JpaRepository<EmailUserTheme, Integer> {
}