package com.github.pius.pichats.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  public final ResponseEntity<?> handleCustomException(CustomException ex){
    CustomExceptionResponse response = new CustomExceptionResponse(ex.getMessage());
    return new ResponseEntity<>(response, ex.getStatus());
  }
}
