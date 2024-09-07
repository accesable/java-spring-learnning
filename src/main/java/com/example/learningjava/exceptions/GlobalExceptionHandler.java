package com.example.learningjava.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle custom exception for product not found
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Product not found");
        body.put("details", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handle custom exception for invalid product data
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Object> handleInvalidProductException(InvalidProductException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Invalid product data");
        body.put("details", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistedException.class)
    public ResponseEntity<Object> handleUserAlreadyExitedException(UserAlreadyExistedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "User Already Exited");
        body.put("details", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Unauthorized");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<Object> handleForbiddenException(HttpClientErrorException.Forbidden ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Unauthorized");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Unauthorized");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Method not found");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }


    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "An unexpected error occurred");
        body.put("details", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
