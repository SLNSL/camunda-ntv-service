package ru.ntv.delegates.telegram;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.entity.articles.Article;
import ru.ntv.service.TelegramUserService;


@Component
@Log4j2
public class GetNewArticle implements JavaDelegate {

    @Autowired
    private TelegramUserService telegramUserService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Article article = (Article) delegateExecution.getVariable("articleObj");
        telegramUserService.sendArticles(article);

        delegateExecution.setVariable("articleHeader", article.getHeader());
    }
}
