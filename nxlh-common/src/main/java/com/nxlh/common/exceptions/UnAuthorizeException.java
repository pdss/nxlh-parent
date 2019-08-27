package com.nxlh.common.exceptions;

/**
 * 未授权异常
 */
public class UnAuthorizeException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnAuthorizeException(String message) {
        super(message);
    }

    public UnAuthorizeException(Throwable throwable) {
        super(throwable);
    }

    public UnAuthorizeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
