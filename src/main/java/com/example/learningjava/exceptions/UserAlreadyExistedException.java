package com.example.learningjava.exceptions;

public class UserAlreadyExistedException extends RuntimeException{
    public UserAlreadyExistedException(String message){
        super(message);
    }
}
