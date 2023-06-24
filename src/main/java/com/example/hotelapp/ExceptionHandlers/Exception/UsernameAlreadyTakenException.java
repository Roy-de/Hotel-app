package com.example.hotelapp.ExceptionHandlers.Exception;

public class UsernameAlreadyTakenException extends RuntimeException{
    public UsernameAlreadyTakenException(String message){
        super(message);
    }
}
