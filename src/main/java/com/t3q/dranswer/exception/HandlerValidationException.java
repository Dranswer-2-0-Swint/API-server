package com.t3q.dranswer.exception;

import com.t3q.dranswer.common.util.ResponseUtil;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.servpot.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class HandlerValidationException {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Object> handleValidationException(Exception ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ApiResponse res = ResponseUtil.parseRspCode(Constants.E40001);

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;
            for (FieldError fieldError : validationException.getBindingResult().getFieldErrors()) {
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        } else if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            for (FieldError fieldError : bindException.getFieldErrors()) {
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        log.warn("\nrspCode : {}\nrspMsg : {}");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
