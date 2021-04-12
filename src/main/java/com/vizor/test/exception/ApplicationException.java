package com.vizor.test.exception;

public class ApplicationException extends RuntimeException {

    /**
     * Instantiates a new Application exception.
     *
     * @param message the message
     * @param ex      the ex
     */
    public ApplicationException(String message, Exception ex) {
        super(message, ex);
    }
}
