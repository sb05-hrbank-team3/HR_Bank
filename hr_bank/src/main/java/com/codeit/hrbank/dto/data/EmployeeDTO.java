package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.Instant;
import lombok.Builder;

@Builder
public record EmployeeDTO(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Instant hireDate,
    String position,
    EmployeeStatus status,
    BinaryContentDTO binaryContent,
    Department department
) {

}
