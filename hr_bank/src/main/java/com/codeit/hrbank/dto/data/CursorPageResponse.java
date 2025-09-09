package com.codeit.hrbank.dto.data;

import java.util.List;
import lombok.Builder;

@Builder
public record CursorPageResponse<T>(
    List<T> content,        // 실제 데이터 리스트
    Object nextCursor,      // 다음 페이지 커서 문자열
    Long nextIdAfter,       // 다음 페이지를 조회할 때 기준이 되는 마지막 요소 ID
    int size,               // 현재 페이지 크기
    long totalElements,     // 전체 요소 수 (선택적으로 계산 가능)
    boolean hasNext         // 다음 페이지 존재 여부
) {}
