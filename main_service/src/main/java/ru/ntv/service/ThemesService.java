package ru.ntv.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.request.journalist.CreateThemeRequest;
import ru.ntv.dto.request.theme.SubOnThemesRequest;
import ru.ntv.dto.response.common.ThemesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.entity.users.TelegramUserAndTheme;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.article.ThemeRepository;
import ru.ntv.repo.user.TelegramUserAndThemeRepository;
import ru.ntv.repo.user.TelegramUserRepository;

import java.util.List;

@Service
public class ThemesService {
    private final ThemeRepository themeRepository;

    private final TelegramUserAndThemeRepository telegramUserAndThemeRepository;

    private final ArticleRepository articleRepository;

    private final TelegramUserRepository telegramUserRepository;

    public ThemesService(ThemeRepository themeRepository, ArticleRepository articleRepository,
                         TelegramUserAndThemeRepository telegramUserAndThemeRepository,
                         TelegramUserRepository telegramUserRepository) {
        this.themeRepository = themeRepository;
        this.articleRepository = articleRepository;
        this.telegramUserAndThemeRepository = telegramUserAndThemeRepository;
        this.telegramUserRepository = telegramUserRepository;
    }

    public ThemesResponse getAllThemes(){
        final var response = new ThemesResponse();
        response.setThemes((List<Theme>) themeRepository.findAll());

        return response;
    }

    public Theme create(CreateThemeRequest req){
        var theme = new Theme();
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
    public boolean subOnThemes(SubOnThemesRequest subOnThemesRequest){
        List<String> themeList = subOnThemesRequest.getThemes();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        String telegramUserName = telegramUserRepository.findByUserLogin(userName).get().getTelegramName();

        themeList.forEach(theme -> {
            TelegramUserAndTheme telegramUserAndTheme = new TelegramUserAndTheme();

            TelegramUserThemeKey telegramUserThemeKey = new TelegramUserThemeKey();
            telegramUserThemeKey.setTelegramUserName(telegramUserName);
            telegramUserThemeKey.setThemeName(theme);

            telegramUserAndTheme.setId(telegramUserThemeKey);

            telegramUserAndThemeRepository.save(telegramUserAndTheme);

        });


        return true;

    }
}