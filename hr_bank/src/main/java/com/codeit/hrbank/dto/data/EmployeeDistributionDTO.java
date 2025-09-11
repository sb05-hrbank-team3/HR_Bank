package com.codeit.hrbank.dto.data;

public record EmployeeDistributionDTO (
    String groupKey,
    Long count,
    double percentage
){
}
