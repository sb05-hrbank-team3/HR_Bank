package com.codeit.hrbank.dto.request;

public record HistoryCreateRequest(
    String propertyName,
    String before,
    String after
) {

}
