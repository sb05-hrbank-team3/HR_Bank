package com.codeit.hrbank.dto.data;

import java.time.LocalDate;
import lombok.Builder;


@Builder
public record DepartmentDTO(
  Long id,
  String name,
  String description,
  LocalDate establishedDate,
  Long employeeCount

){}
