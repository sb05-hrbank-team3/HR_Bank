package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.response.CursorPageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

    // page 기반
    default <T> CursorPageResponse<T> fromPage(Page<T> page) {
        return CursorPageResponse.<T>builder()
            .content(page.getContent())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .hasNext(page.hasNext())
            .build();
    }
}
