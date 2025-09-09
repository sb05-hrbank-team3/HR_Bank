package com.codeit.hrbank.dto.request;

public record HistoryUpdateRequest(
    String propertyName,
    String before,
    String after
) {

}
