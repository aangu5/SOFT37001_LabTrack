package org.ordep.labtrack.controller;

import org.ordep.labtrack.exception.UserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ModelAndView resolveUserException(UserException e) {
        return new ModelAndView("redirect:/error");
    }
}
