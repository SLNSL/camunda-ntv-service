package ru.ntv.delegates.sender;


import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.service.ArticleService;

import javax.inject.Named;

@Component
@Log4j2
public class MailSender implements JavaDelegate {

    @Autowired
    private ArticleService articleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        articleService.sendDigest();
    }
}
