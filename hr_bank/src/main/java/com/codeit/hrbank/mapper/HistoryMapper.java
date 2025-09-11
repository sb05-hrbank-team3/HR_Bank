package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.entity.History;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

  HistoryDTO toDTO(History history);

}
