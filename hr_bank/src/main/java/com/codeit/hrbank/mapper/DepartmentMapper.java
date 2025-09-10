package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentDTO toDTO(Department entity);

  DepartmentDTO toDepartmentDTO(Department department);

}
