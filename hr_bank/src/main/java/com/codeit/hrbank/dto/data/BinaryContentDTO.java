package com.codeit.hrbank.dto.data;

import jakarta.persistence.Column;

public record BinaryContentDTO(
    Long id,
    String name,
    Long size,
    String contentType
)
{}
