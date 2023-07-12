package com.example.voting.Exceptions;

public class TokenValidationException extends RuntimeException{
    public final String errorMessage;

    public TokenValidationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
