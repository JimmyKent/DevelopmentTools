package com.jimmy.development.tools;


public class ReflectException extends RuntimeException {
    public ReflectException(Throwable cause) {
        super(cause);
    }

    public ReflectException(String msg) {
        super(msg);
    }
}
