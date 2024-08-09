package com.share.security.rest.api.exception;

public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException() {
    }

    public ResourceConflictException(String message) {
        super(message);
    }
}