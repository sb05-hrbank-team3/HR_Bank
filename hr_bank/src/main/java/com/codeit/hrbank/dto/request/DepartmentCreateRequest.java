package com.codeit.hrbank.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record DepartmentCreateRequest (
  String name,
  String description,
  String establishedDate
)
{}