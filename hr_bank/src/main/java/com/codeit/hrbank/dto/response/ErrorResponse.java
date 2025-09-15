package com.codeit.hrbank.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timeStamp,
    int status,
    String message,
    String details
) {
  public static ErrorResponse of(int status, String message, String details) {
    return new ErrorResponse(LocalDateTime.now(), status, message, details);
  }
}
