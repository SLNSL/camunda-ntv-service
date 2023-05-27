package ru.ntv.exception;

import org.camunda.bpm.engine.delegate.BpmnError;

public class NotRegisteredException extends BpmnError {

    public NotRegisteredException() {
        super("NotRegistered");
    }
}
