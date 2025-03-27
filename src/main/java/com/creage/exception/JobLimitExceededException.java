package com.creage.exception;

public class JobLimitExceededException extends RuntimeException {
    public JobLimitExceededException(String message) {
        super(message);
    }
}
