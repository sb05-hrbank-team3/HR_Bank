package com.codeit.hrbank.dto.data;

public record EmployeeTrendDTO(
    String date, // 시작일
    Long count,
    Long change, // 이전 대비 증감
    Double changeRate // 이전 대비 증감률
) {

}
