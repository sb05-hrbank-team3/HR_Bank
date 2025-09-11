package com.codeit.hrbank.dto.data;

import java.time.LocalDate;

public record EmployeeTrendDTO(
    LocalDate date, // 시작일
    Long count,
    Long change, // 이전 대비 증감
    Double changeRate // 이전 대비 증감률
) {

}
