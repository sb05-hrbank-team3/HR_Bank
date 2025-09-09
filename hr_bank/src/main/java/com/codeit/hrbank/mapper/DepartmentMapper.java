package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.entity.Department;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.*;
import java.time.format.*;


@Mapper(componentModel = "spring" ,builder = @Builder(disableBuilder = false) , imports = {LocalDate.class, ZoneId.class})
public interface DepartmentMapper {

  @Mapping(target = "establishedDate",
      expression = "java(dto.establishedDate() == null ? null : " +
          "LocalDate.parse(dto.establishedDate()).atStartOfDay(ZoneId.systemDefault()).toInstant())")
  Department toEntity(DepartmentCreateRequest dto);

  DepartmentDTO toDTO(Department entity);

  DepartmentDTO toDepartmentDTO(Department department);

  Department toDepartment(Long id);

  Department toDepartment(DepartmentUpdateRequest departmentUpdateRequest);


}
