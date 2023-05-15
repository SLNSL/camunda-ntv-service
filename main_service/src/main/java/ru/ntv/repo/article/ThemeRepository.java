package ru.ntv.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.articles.Theme;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    Optional<Theme> findByThemeName(String themeName);
}