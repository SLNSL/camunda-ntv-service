package ru.ntv.exception;

public class ArticleNotFoundException extends Exception{
    public ArticleNotFoundException(String msg){
        super(msg);
    }
}