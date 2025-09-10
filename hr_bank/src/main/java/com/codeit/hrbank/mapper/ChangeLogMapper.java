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

  ChangeLogDTO toDto(ChangeLog changeLog);

  @Mapping(source = "employee", target = "employee")
  @Mapping(source = "ip", target = "ipAddress")
  @Mapping(source = "at", target = "at")
  ChangeLog toEntity(ChangeLogCreateRequest request, Employee employee, String ip, Instant at);
}
