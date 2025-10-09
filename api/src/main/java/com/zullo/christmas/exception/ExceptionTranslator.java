package com.zullo.christmas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@RestControllerAdvice
public class ExceptionTranslator {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionTranslator.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse processException(Exception e){
        LOG.error("Generic unhandled error occurred in the application!", e);
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error, contact developer!");
    }

    @ExceptionHandler(ChristmasException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse processApplicationException(ChristmasException e){
        LOG.error("Error occurred in the application!", e);
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error, contact developer!");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse processRuntimeException(RuntimeException e){
        LOG.error("Error occurred in the application!", e);
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error, contact developer!");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse processExpiredJwt(ExpiredJwtException e){
        return ErrorResponse.create(e, HttpStatus.UNAUTHORIZED, "JWT Expired!");
    }
    
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse processExpiredJwt(MalformedJwtException e){
        return ErrorResponse.create(e, HttpStatus.UNAUTHORIZED, "JWT is Invalid!");
    }

}
