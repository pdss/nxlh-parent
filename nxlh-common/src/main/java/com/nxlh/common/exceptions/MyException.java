package com.nxlh.common.exceptions;

public class MyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MyException(String message) {
        super(message);
    }

    public MyException(Throwable throwable) {
        super(throwable);
    }

    public MyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
