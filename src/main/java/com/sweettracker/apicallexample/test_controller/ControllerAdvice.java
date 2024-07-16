package com.sweettracker.apicallexample.test_controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @InitBinder
    public void initBInder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
