package com.codeit.hrbank.dto.request;

import com.codeit.hrbank.entity.ChangeLogType;
import java.util.List;

public record ChangeLogCreateRequest(
    ChangeLogType type,
    String employeeNumber,
    String memo
//    List<HistoryDTO> histories
) {

}
