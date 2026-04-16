package com.smartcampus.exceptions;

/**
 * Exception thrown when attempting to send a reading to a sensor that is in MAINTENANCE or OFFLINE status.
 */
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String message) {
        super(message);
    }
}
