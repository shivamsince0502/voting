package com.example.voting.Exceptions;

public class AuthenticationFailed extends RuntimeException{
    public final String errorMessage;

    public AuthenticationFailed(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
