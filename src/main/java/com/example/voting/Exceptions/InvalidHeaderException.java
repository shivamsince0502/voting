package com.example.voting.Exceptions;

public class InvalidHeaderException extends RuntimeException{
    public final String errorMessage;

    public InvalidHeaderException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
