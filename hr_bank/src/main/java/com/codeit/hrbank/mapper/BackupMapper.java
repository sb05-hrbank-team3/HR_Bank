package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.BackupDTO;
import com.codeit.hrbank.entity.Backup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BackupMapper {


  Backup toEntity(BackupDTO dto);

  @Mapping(target = "fileId", source = "file.id")
  BackupDTO toDto(Backup entity);



}
