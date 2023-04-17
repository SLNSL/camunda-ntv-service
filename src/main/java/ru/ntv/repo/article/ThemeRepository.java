package ru.ntv.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.articles.Theme;


@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {
}

