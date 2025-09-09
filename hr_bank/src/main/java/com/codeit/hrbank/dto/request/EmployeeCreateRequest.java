package com.codeit.hrbank.dto.request;

import java.time.Instant;

public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    Instant hireDate,
    String memo
) { }
