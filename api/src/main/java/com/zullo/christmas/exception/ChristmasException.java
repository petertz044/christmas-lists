package com.zullo.christmas.exception;

public class ChristmasException extends RuntimeException {
    public ChristmasException(){
        super();
    }
    public ChristmasException(String message){
        super(message);
    }
    public ChristmasException(String message, Throwable cause){
        super(message, cause);
    }


}
