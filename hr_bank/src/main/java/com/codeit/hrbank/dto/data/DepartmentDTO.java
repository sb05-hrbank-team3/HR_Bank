package com.codeit.hrbank.dto.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



public record DepartmentDTO(
  Long id,
  String name,
  String description,
  String establishedDate,
  Integer employeeCount

){}
