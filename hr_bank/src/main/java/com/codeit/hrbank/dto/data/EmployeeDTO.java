package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.Instant;
import lombok.Builder;

@Builder
public record EmployeeDTO(
    Long id,
    String name,
    String email,
    String employeeNumber,
    String position,
    Instant hireDate,
    EmployeeStatus status,
    Long profileImageId,
    Long departmentId,
    String departmentName
) {

}
