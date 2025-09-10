package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.entity.Department;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {




  public DepartmentDTO create(DepartmentCreateRequest request);

  public List<DepartmentDTO> findAll(String nameOrDescription,
      Long idAfter,
      Instant cursor,
      int size,
      String sortField,
      String sortDirection);

  public Optional<DepartmentDTO> findById(Long id);

  public void deleteById(Long id);

  public DepartmentDTO update(Long id,DepartmentUpdateRequest request);






}
