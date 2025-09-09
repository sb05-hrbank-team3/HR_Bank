package com.codeit.hrbank.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



public record DepartmentUpdateRequest (
  String name,
  String description,
  String establishedDate

){}
