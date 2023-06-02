package ru.ntv.delegates.articles;


import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.dto.response.common.ArticlesResponse;
import ru.ntv.service.ArticleService;

@Component
@Log4j2
public class GetAllArticles implements JavaDelegate {

    @Autowired
    private ArticleService articleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer offset = Integer.parseInt((String) delegateExecution.getVariable("offset"));
        Integer limit = Integer.parseInt((String) delegateExecution.getVariable("limit"));

        ArticlesResponse articlesResponse = articleService.getAll(offset, limit);

        delegateExecution.setVariable("n", articlesResponse.getArticles().size());
        delegateExecution.setVariable("articles", articlesResponse.getArticles().toString());
        log.error(articlesResponse.toString());
    }
}