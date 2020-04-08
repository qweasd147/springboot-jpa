package com.example.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity handleRuntimeException(RuntimeException ex){

        logger.error("RuntimeException!!!");
        logger.error(ex.getMessage());

        Map<String, String> result = new HashMap<>();

        result.put("msg","에러남");

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex
            , HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error("handleBindException!!!");

        return super.handleBindException(ex, headers, status, request);
    }
}
