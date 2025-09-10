package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.entity.ChangeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  @Mapping(target = "employeeNumber", source = "employee.employeeNumber")
  @Mapping(target = "histories", ignore = true) // 필요하면 별도 매핑 로직 추가
  ChangeLogDTO toDto(ChangeLog changeLog);
}
