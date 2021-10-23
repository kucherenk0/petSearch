package com.kowechka.petsearch.service.exception;

public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileStorageException(String message, Exception cause) {
        super(message, cause);
    }
}
