package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.data.HistoryDTO;
import com.codeit.hrbank.dto.request.HistoryCreateRequest;
import com.codeit.hrbank.dto.request.HistoryUpdateRequest;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.History;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

  HistoryDTO toDTO(History history);

  @Mapping(source = "changeLog", target = "changeLogs")
  History toEntity(HistoryCreateRequest request, ChangeLog changeLog);

  @Mapping(source = "changeLog", target = "changeLogs")
  @Mapping(target = "id", ignore = true)
  History toEntity(HistoryDTO dto, ChangeLog changeLog);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // request가 Null -> 기존 엔티티 값 그대로 유지
  void updateEntityFromRequest(HistoryUpdateRequest request, @MappingTarget History history);

}
