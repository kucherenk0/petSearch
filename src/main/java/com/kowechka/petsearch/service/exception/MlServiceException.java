package com.kowechka.petsearch.service.exception;

public class MlServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MlServiceException(String message, Exception cause) {
        super(message, cause);
    }

    public MlServiceException(String message) {
        super(message);
    }
}
