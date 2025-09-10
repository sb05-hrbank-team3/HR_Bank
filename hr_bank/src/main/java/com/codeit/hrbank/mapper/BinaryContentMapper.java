package com.codeit.hrbank.mapper;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.entity.BinaryContent;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  public static BinaryContentDTO toDTO(BinaryContent entity) {
    if (entity == null) {
      return null;
    }
    return new BinaryContentDTO(entity.getId(), entity.getName(), entity.getSize(),
        entity.getContentType());
  }
}