package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice

public class DharmaException {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView processException(RuntimeException exception){
        ModelAndView m = new ModelAndView();
        m.addObject("dharmaException",exception.getMessage());
        m.setViewName("/error/500");
        return m;
    }

}
