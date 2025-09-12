package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.ChangeLogDTO;
import com.codeit.hrbank.entity.ChangeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

  ChangeLogDTO toDto(ChangeLog changeLog);
}
