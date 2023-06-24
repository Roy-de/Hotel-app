package com.example.hotelapp.ExceptionHandlers.Exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String message){
        super(message);
    }
}
