package com.codeit.hrbank.repository;

import com.codeit.hrbank.dto.request.ChangeLogListRequest;
import com.codeit.hrbank.dto.response.ChangeLogListResponse;

public interface ChangeLogQueryRepository {
  ChangeLogListResponse search(ChangeLogListRequest request);
}
