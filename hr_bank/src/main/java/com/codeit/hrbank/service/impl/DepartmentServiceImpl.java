package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.config.LocalDateToInstantDeserializer;
import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.mapper.DepartmentMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.service.DepartmentService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;


  @Override
  public DepartmentDTO create(DepartmentCreateRequest request) {
    Department department= departmentMapper.toEntity(request);

    return departmentMapper.toDTO(departmentRepository.save(department));
  }

  @Override
  public List<DepartmentDTO> findAll(
      String nameOrDescription,
      Long idAfter,
      Instant cursor,
      int size,
      String sortField,
      String sortDirection

  ) {
    List<DepartmentDTO> dtos = Optional.ofNullable(
            departmentRepository.findAndSortDepartments(
                nameOrDescription, cursor, idAfter, size, sortField, sortDirection
            )
        )
        .orElse(Collections.emptyList())   // null일 경우 빈 리스트 반환
        .stream()
        .map(departmentMapper::toDTO)
        .toList();


    return dtos;
  }


  @Override
  public Optional<DepartmentDTO> findById(Long id) {
    return departmentRepository.findById(id).map(departmentMapper::toDepartmentDTO);
  }

  @Override
  public void deleteById(Long id) {
    Department department=departmentRepository
        .findById(id).orElseThrow(()->new NoSuchElementException("삭제 대상 부서가 없습니다: " + id));
    departmentRepository.deleteById(id);

  }


  @Override
  public DepartmentDTO update(Long id,DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다: " + id));
    department.setName(request.name());
    department.setDescription(request.description());

    department.setEstablishedDate( request.establishedDate());

    return departmentMapper.toDepartmentDTO(departmentRepository.save(department));
  }
}
