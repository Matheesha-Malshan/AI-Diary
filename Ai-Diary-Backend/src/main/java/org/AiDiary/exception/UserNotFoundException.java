package org.AiDiary.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String error){
        super(error);
    }
}
