package com.codeit.hrbank.dto.request;

import com.codeit.hrbank.config.LocalDateToInstantDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



public record DepartmentUpdateRequest (
  String name,
  String description,

  @JsonDeserialize(using = LocalDateToInstantDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  Instant establishedDate

){}
