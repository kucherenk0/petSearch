package com.kowechka.petsearch.service.exception;

public class CantGetCurrentUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CantGetCurrentUserException() {
        super("Can't get current user");
    }
}
