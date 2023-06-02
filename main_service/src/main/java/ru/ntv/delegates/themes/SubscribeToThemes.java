package ru.ntv.delegates.themes;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.service.ThemesService;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class SubscribeToThemes implements JavaDelegate {

    @Autowired
    private ThemesService themesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String themesIdString = (String) delegateExecution.getVariable("Темы");
        String username = (String) delegateExecution.getVariable("username");
        List<Integer> themesId = Arrays.stream(themesIdString.split(", ")).map(Integer::parseInt).toList();
        
        themesService.subscribeToThemes(themesId, username);
    }
}