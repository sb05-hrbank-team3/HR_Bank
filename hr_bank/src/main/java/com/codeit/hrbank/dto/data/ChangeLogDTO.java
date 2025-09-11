package com.codeit.hrbank.dto.data;

import com.codeit.hrbank.entity.ChangeLogType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record ChangeLogDTO(
    Long id,
    ChangeLogType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at
) {

}
