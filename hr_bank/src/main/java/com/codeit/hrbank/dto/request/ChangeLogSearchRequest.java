package com.codeit.hrbank.dto.request;

import com.codeit.hrbank.entity.ChangeLogType;
import java.time.LocalDate;

public record ChangeLogSearchRequest(
    String employeeNumber,
    String memo,
    String ipAddress,
    ChangeLogType type,
    LocalDate atFrom,
    LocalDate atTo,

    Long idAfter,
    String cursor,

    int size,
    String sortField,
    String sortDirection
) {
  public ChangeLogSearchRequest {
    if (size <= 0) size = 10;
    if (sortField == null || sortField.isBlank()) sortField = "at";
    if (sortDirection == null || sortDirection.isBlank()) sortDirection = "desc";
  }
}
