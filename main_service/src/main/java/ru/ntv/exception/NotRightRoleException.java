package ru.ntv.exception;

import java.util.NoSuchElementException;

public class NotRightRoleException extends NoSuchElementException {
    public NotRightRoleException(String msg){
        super(msg);
    }
}