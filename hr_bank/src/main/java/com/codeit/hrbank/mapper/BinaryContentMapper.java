package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.response.BinaryContentResponse;
import com.codeit.hrbank.entity.BinaryContent;

public class BinaryContentMapper {

  public static BinaryContentDTO toDTO(BinaryContent entity) {
    if (entity == null) {
      return null;
    }
    return new BinaryContentDTO(entity.getId(), entity.getName(), entity.getSize(),
        entity.getContentType());
  }

  public static BinaryContentResponse toResponse(BinaryContentDTO dto) {
    if (dto == null) {
      return null;
    }
    return new BinaryContentResponse(dto.id(), dto.name(), dto.size(), dto.contentType());
  }
}