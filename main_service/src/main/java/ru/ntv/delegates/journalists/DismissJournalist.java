package ru.ntv.delegates.journalists;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.service.UserService;

@Component
public class DismissJournalist implements JavaDelegate {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int journalistId = (int) delegateExecution.getVariable("journalistId");
        
        try {
            userService.dismissJournalist(journalistId);
        }catch (Throwable e){
            throw new BpmnError("Не удалось уволить");
        }
    }
}