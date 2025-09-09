package com.codeit.hrbank.dto.data;

public record HistoryDTO(
    Long id,
    String propertyName,
    String before,
    String after
) {

}
