package ru.ntv.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.request.journalist.CreateThemeRequest;
import ru.ntv.dto.response.common.ThemesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.entity.users.TelegramUserAndTheme;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.article.ThemeRepository;
import ru.ntv.repo.user.TelegramUserAndThemeRepository;
import ru.ntv.repo.user.TelegramUserRepository;
import ru.ntv.repo.user.UserRepository;

import java.util.List;

@Service
public class ThemesService {
    private final ThemeRepository themeRepository;

    private final TelegramUserAndThemeRepository telegramUserAndThemeRepository;

    private final ArticleRepository articleRepository;

    private final TelegramUserRepository telegramUserRepository;

    private final UserRepository userRepository;

    public ThemesService (
            ThemeRepository themeRepository,
            ArticleRepository articleRepository,
            TelegramUserAndThemeRepository telegramUserAndThemeRepository,
            TelegramUserRepository telegramUserRepository,
            UserRepository userRepository) {
        this.themeRepository = themeRepository;
        this.articleRepository = articleRepository;
        this.telegramUserAndThemeRepository = telegramUserAndThemeRepository;
        this.telegramUserRepository = telegramUserRepository;
        this.userRepository = userRepository;
    }

    public ThemesResponse getAllThemes(){
        final var response = new ThemesResponse();
        response.setThemes(themeRepository.findAll());

        return response;
    }

    public Theme create(CreateThemeRequest req){
        final var theme = new Theme();
        theme.setThemeName(req.getName());
        
        return themeRepository.save(theme);
    }

    public void delete(int id){
        final var theme = themeRepository.findById(id).orElse(null);

        if (theme == null) return;

        List<Article> articles = theme.getArticles();
        for (Article article : articles) {
            article.getThemes().remove(theme);
            articleRepository.save(article);
        }

        themeRepository.delete(theme);
    }
    
    @Transactional
    public void subscribeToThemes(List<Integer> themeIds){
        final var userName = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        final var user = userRepository.findByLogin(userName).get();

        final var telegramUser = telegramUserRepository
                .findByUserId(user.getId())
                .get();

        final var themes = themeRepository.findAllById(themeIds);

        themes.forEach(theme -> {
            TelegramUserThemeKey telegramUserThemeKey = new TelegramUserThemeKey();
            telegramUserThemeKey.setTelegramUserId(telegramUser.getId());
            telegramUserThemeKey.setThemeId(theme.getId());

            TelegramUserAndTheme telegramUserAndTheme = new TelegramUserAndTheme();
            telegramUserAndTheme.setId(telegramUserThemeKey);
            telegramUserAndThemeRepository.save(telegramUserAndTheme);
        });
    }
}