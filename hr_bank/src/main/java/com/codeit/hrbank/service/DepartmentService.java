package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import java.util.Optional;

public interface DepartmentService {


  DepartmentDTO create(DepartmentCreateRequest request);

  CursorPageResponse<DepartmentDTO> findAll(String nameOrDescription,
      Long idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection);

  Optional<DepartmentDTO> findById(Long id);

  void deleteById(Long id);

  DepartmentDTO update(Long id, DepartmentUpdateRequest request);


}
