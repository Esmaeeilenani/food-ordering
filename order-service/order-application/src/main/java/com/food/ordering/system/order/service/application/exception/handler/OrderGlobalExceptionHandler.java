package com.food.ordering.system.order.service.application.exception.handler;

import com.food.ordering.system.application.handler.ErrorDTO;
import com.food.ordering.system.application.handler.GlobalExceptionHandler;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordering.system.order.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {



     @ExceptionHandler(OrderDomainException.class)
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     public ErrorDTO handleException(OrderDomainException ex) {
          log.error("Exception occurred: {}", ex.getMessage());
          return ErrorDTO.builder()
                  .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                  .message(ex.getMessage())
                  .build();
     }

     @ExceptionHandler(OrderNotFoundException.class)
     @ResponseStatus(HttpStatus.NOT_FOUND)
     public ErrorDTO handleException(OrderNotFoundException ex) {
          log.error("Exception occurred: {}", ex.getMessage());
          return ErrorDTO.builder()
                  .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                  .message(ex.getMessage())
                  .build();
     }



}
