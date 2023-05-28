package ru.ntv.exception;

import org.camunda.bpm.engine.delegate.BpmnError;

public class NotRegisteredException extends Exception {

    public NotRegisteredException() {
        super("NotRegistered");
    }
}
