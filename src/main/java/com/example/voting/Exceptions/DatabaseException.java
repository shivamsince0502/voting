package com.example.voting.Exceptions;

public class DatabaseException extends RuntimeException{
    public final String errorMessage;

    public DatabaseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }



}
