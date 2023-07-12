package com.example.voting.Exceptions;

public class BadRequestException extends RuntimeException{
    public final String errorCode;
    public final String errorMessage;

    public BadRequestException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }



    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
