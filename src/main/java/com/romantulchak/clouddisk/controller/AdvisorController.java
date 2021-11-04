package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.exception.FileUploadException;
import com.romantulchak.clouddisk.exception.FileWithNameAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdvisorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = FileWithNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleFileWithNameAlreadyExistsException(FileWithNameAlreadyExistsException ex, WebRequest webRequest){
        Map<Object, Object> body = getBody(ex);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<Object> handleFileUploadException(FileUploadException ex, WebRequest webRequest){
        Map<Object, Object> body = getBody(ex);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    private Map<Object, Object> getBody(RuntimeException ex){
        Map<Object, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("date", LocalDateTime.now());
        return body;
    }

}

