package com.codeit.hrbank.mapper;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.response.BinaryContentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDTO toDTO(BinaryContent entity);

  BinaryContentResponse toEntity(BinaryContentDTO dto);
}