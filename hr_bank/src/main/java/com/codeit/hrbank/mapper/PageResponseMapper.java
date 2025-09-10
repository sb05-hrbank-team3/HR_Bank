package com.codeit.hrbank.mapper;

import com.codeit.hrbank.dto.response.CursorPageResponse;
import java.util.List;
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

    default <T> CursorPageResponse<T> fromCursor(List<T> content, int size, String nextCursor) {
        boolean hasNext = content.size() == size; // 요청한 size만큼 조회되면 다음 페이지 존재 추정
        return CursorPageResponse.<T>builder()
            .content(content)
            .nextCursor(nextCursor)
            .size(size)
            .hasNext(hasNext)
            .build();
    }
}
