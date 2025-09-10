package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


  @Mapping(target = "profileImageId", source = "binaryContent.id")
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "departmentName", source = "department.name")
  EmployeeDTO toDTO(Employee employee);

  Employee toEntity(EmployeeDTO employeeDTO);
}
