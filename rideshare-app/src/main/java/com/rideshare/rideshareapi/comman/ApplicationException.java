package com.rideshare.rideshareapi.comman;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ApplicationException extends RuntimeException{
    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(){}

}
