package com.codeit.hrbank.dto.request;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    LocalDate hireDate,
    String memo
) { }
