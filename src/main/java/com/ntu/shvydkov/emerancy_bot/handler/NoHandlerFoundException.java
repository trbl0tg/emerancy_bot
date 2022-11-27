package com.ntu.shvydkov.emerancy_bot.handler;

public class NoHandlerFoundException extends Exception {

    public NoHandlerFoundException() {
    }

    public NoHandlerFoundException(String message) {
        super(message);
    }

    public NoHandlerFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoHandlerFoundException(Throwable cause) {
        super(cause);
    }

}
