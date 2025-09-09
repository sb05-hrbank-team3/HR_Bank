package com.codeit.hrbank.dto.request;

public record HistoryCreateRequest(
    Long changeLogId,
    String propertyName,
    String before,
    String after
) {

}
