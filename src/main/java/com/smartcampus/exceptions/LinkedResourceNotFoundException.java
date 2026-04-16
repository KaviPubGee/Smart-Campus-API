package com.smartcampus.exceptions;

/**
 * Custom exception thrown when attempting to logically connect an entity to a parent resource that doesn't exist.
 */
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}
