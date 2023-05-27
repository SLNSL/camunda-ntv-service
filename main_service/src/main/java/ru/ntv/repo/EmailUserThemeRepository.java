package ru.ntv.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.entity.users.EmailUserTheme;

public interface EmailUserThemeRepository extends JpaRepository<EmailUserTheme, Integer> {
}