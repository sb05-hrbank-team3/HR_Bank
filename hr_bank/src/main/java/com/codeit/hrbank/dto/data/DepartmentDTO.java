package com.codeit.hrbank.dto.data;

import java.time.Instant;
import lombok.Builder;


@Builder
public record DepartmentDTO(
  Long id,
  String name,
  String description,
  Instant establishedDate,
  Long employeeCount

){}
