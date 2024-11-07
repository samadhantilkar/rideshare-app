package com.rideshare.rideshareapi.comman.model;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ApplicationException extends RuntimeException{
    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(){}

}
