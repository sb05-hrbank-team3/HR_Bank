package com.codeit.hrbank.dto.request;

import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    EmployeeStatus status,
    LocalDate hireDate,
    String memo
) { }
