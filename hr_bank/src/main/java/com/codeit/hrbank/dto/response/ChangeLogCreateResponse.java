package com.codeit.hrbank.dto.response;

import com.codeit.hrbank.entity.ChangeLogType;
import java.time.Instant;

public record ChangeLogCreateResponse(
    Long id,
    ChangeLogType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at
) {

}
