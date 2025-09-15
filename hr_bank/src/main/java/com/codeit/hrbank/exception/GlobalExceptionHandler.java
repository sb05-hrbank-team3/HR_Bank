package com.codeit.hrbank.exception;

import com.codeit.hrbank.dto.response.ErrorResponse;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String details) {
    ErrorResponse response = ErrorResponse.of(status.value(), message, details);
    return ResponseEntity.status(status).body(response);
  }

  // 전역 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    // 500 Internal Server Error
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getSimpleName(), e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    // 400 Bad Request
    return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(), e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    // 404 Not Found
    return buildErrorResponse(HttpStatus.NOT_FOUND, e.getClass().getSimpleName(), e.getMessage());
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException e) {
    // 404 Not Found
    return buildErrorResponse(HttpStatus.NOT_FOUND, e.getClass().getSimpleName(), e.getMessage());
  }
}
