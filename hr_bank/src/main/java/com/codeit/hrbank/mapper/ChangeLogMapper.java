package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.entity.ChangeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  @Mapping(target = "employeeNumber", source = "employee.employeeNumber")
  ChangeLogDTO toDto(ChangeLog changeLog);
}
