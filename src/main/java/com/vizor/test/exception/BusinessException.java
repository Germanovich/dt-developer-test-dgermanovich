package com.vizor.test.exception;

public class BusinessException extends RuntimeException {

    /**
     * Instantiates a new BusinessException.
     *
     * @param message the message
     */
    public BusinessException(String message) {
        super(message);
    }
}
