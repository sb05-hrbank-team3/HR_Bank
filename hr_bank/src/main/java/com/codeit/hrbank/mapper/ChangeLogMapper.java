package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.dto.request.ChangeLogCreateRequest;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.Employee;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  @Mapping(target = "employeeNumber", source = "employee.employeeNumber")
  @Mapping(target = "histories", ignore = true) // 필요하면 별도 매핑 로직 추가
  ChangeLogDTO toDto(ChangeLog changeLog);

  @Mapping(source = "employee", target = "employee")
  @Mapping(source = "ip", target = "ipAddress")
  @Mapping(source = "at", target = "at")
  ChangeLog toEntity(ChangeLogCreateRequest request, Employee employee, String ip, Instant at);
}
