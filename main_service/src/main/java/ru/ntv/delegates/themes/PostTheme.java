package ru.ntv.delegates.themes;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.dto.request.journalist.CreateThemeRequest;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.service.ArticleService;
import ru.ntv.service.ThemesService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostTheme implements JavaDelegate {

    @Autowired
    private ThemesService themesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("themeName");

        CreateThemeRequest createThemeRequest = new CreateThemeRequest(name);
        Theme theme = themesService.create(createThemeRequest);



        delegateExecution.setVariable("themeId", theme.getId());


    }
}
