package ru.ntv.service;


import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ru.ntv.dto.request.journalist.CreateThemeRequest;
import ru.ntv.dto.response.common.ThemesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.entity.users.EmailUserTheme;
import ru.ntv.entity.users.TelegramUserAndTheme;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;
import ru.ntv.exception.NotRegisteredException;
import ru.ntv.repo.*;

import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
public class ThemesService {
    private final ThemeRepository themeRepository;
    private final TelegramUserAndThemeRepository telegramUserAndThemeRepository;
    private final ArticleRepository articleRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final UserService userService;
    private final EmailUserRepository emailUserRepository;
    private final EmailUserThemeRepository emailUserThemeRepository;

    private final PlatformTransactionManager platformTransactionManager;

    public ThemesService(
            ThemeRepository themeRepository,
            ArticleRepository articleRepository,
            TelegramUserAndThemeRepository telegramUserAndThemeRepository,
            TelegramUserRepository telegramUserRepository,
            UserService userService,
            EmailUserRepository emailUserRepository,
            EmailUserThemeRepository emailUserThemeRepository,
            PlatformTransactionManager platformTransactionManager) {

        this.themeRepository = themeRepository;
        this.articleRepository = articleRepository;
        this.telegramUserAndThemeRepository = telegramUserAndThemeRepository;
        this.telegramUserRepository = telegramUserRepository;
        this.userService = userService;
        this.emailUserRepository = emailUserRepository;
        this.emailUserThemeRepository = emailUserThemeRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    public ThemesResponse getAllThemes() {
        final var response = new ThemesResponse();
        response.setThemes(themeRepository.findAll());

        return response;
    }

    public Theme create(CreateThemeRequest req) {
        final var theme = new Theme();
        theme.setThemeName(req.getName());

        return themeRepository.save(theme);
    }

    public void delete(int id) {
        final var theme = themeRepository.findById(id).orElse(null);

        if (theme == null) return;

        List<Article> articles = theme.getArticles();
        for (Article article : articles) {
            article.getThemes().remove(theme);
            articleRepository.save(article);
        }

        themeRepository.delete(theme);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void subscribeToThemes(List<Integer> themeIds, String username) throws Exception {
            try {
                final var user = userService.findByLogin(username);
                final var telegramUser = telegramUserRepository.findByUserId(user.getId()).get();
                log.error(telegramUser.getTelegramChatId());
                if (telegramUser.getTelegramChatId() == null) throw new BpmnError("TelegramNotRegistered");

                final var themes = themeRepository.findAllById(themeIds);

                themes.forEach(theme -> {
                    TelegramUserThemeKey telegramUserThemeKey = new TelegramUserThemeKey();
                    telegramUserThemeKey.setTelegramUserId(telegramUser.getId());
                    telegramUserThemeKey.setThemeId(theme.getId());

                    TelegramUserAndTheme telegramUserAndTheme = new TelegramUserAndTheme();
                    telegramUserAndTheme.setId(telegramUserThemeKey);
                    telegramUserAndThemeRepository.save(telegramUserAndTheme);
                    final var emailUserTheme = new EmailUserTheme(user.getId(), theme.getId());
                    emailUserThemeRepository.save(emailUserTheme);
                });
            } catch (Exception e){
                if (e instanceof BpmnError) throw e;
                throw new BpmnError(e.getMessage());
            }

    }

    public void unsubscribeFromAllThemes(String username) {
        final var user = userService.findByLogin(username);
        final var telegramUser = telegramUserRepository
                .findByUserId(user.getId())
                .get();

        telegramUserRepository.deleteAllByTelegramName(telegramUser.getTelegramName());
        emailUserRepository.deleteAllByUserId(user.getId());
    }
}