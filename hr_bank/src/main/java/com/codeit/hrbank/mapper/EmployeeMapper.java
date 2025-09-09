package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  EmployeeDTO toDTO(Employee employee);

  Employee toEntity(EmployeeDTO employeeDTO);
}
