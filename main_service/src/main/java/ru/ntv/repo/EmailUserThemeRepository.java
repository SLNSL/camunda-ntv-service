package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntv.entity.users.EmailUserTheme;

public interface EmailUserThemeRepository extends JpaRepository<EmailUserTheme, Integer> {
}