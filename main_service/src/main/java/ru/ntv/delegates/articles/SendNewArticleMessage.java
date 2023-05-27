package ru.ntv.delegates.articles;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Component
@Log4j2
public class SendNewArticleMessage implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info(delegateExecution.getCurrentActivityName());

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("postArticle")
                .setVariable("articleObj",delegateExecution.getVariable("articleObj"))
                .correlate();
    }
}
