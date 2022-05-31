package com.nikos.posts.exception;


import java.security.InvalidParameterException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;
import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nikos.posts.payload.response.ErrorResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleUnknownServiceDomainException(Exception ex, HttpServletRequest  request) {
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity handleAuthorizationException(AuthenticationException ex, HttpServletRequest  request) {
        log.error("", ex);
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.FORBIDDEN);
    }

    
    @ExceptionHandler({AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity handleAuthorizationException(AuthorizationException ex, HttpServletRequest  request) {
        log.error("", ex);
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest  request) {
        log.error("", ex);
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.UNAUTHORIZED);
    }

    

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleEntryNotFoundException(ResourceNotFoundException ex, HttpServletRequest  request) {
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleEntryNotFoundException(ValidationException ex, HttpServletRequest  request) {
        return buildErrorResourceResponseEntity(request,ex, HttpStatus.CONFLICT);
    }

    
    private ErrorResponseBody buildErrorResponseBody(HttpServletRequest request, Exception ex, HttpStatus httpStatus) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.setTimestamp(new Date());
        errorResponseBody.setStatus(httpStatus.value());
        errorResponseBody.setException(ex.getClass().getName());
        errorResponseBody.setMessage(ex.getMessage());
    
        return errorResponseBody;
    }

    private ResponseEntity<ErrorResponseBody> buildErrorResourceResponseEntity(HttpServletRequest request,
                                                                           ResourceNotFoundException ex,
                                                                           HttpStatus httpStatus) {
        ErrorResponseBody errorResponseBody = buildErrorResponseBody(request,ex,httpStatus);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<ErrorResponseBody>(errorResponseBody, responseHeaders, httpStatus);
    }



    private ResponseEntity<ErrorResponseBody> buildErrorResourceResponseEntity(HttpServletRequest request,
                                                                               Exception ex,
                                                                               HttpStatus httpStatus) {
        ErrorResponseBody errorResponseBody = buildErrorResponseBody(request,ex,httpStatus);

        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<ErrorResponseBody>(errorResponseBody, responseHeaders, httpStatus);
    }

    private ResponseEntity<Object> buildErrorResourceResponseEntityForNoHandlerFound(WebRequest request,
                                                                                    NoHandlerFoundException ex,
                                                                                    HttpStatus httpStatus) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.setTimestamp(new Date());
        errorResponseBody.setStatus(httpStatus.value());
        errorResponseBody.setException(ex.getClass().getName());
        errorResponseBody.setMessage(ex.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<Object>(errorResponseBody, responseHeaders, httpStatus);
    }

    

}
