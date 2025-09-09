package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.mapper.DepartmentMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.service.DepartmentService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    LocalDate localDate = LocalDate.parse(request.establishedDate());

    department.setEstablishedDate( localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    return departmentMapper.toDepartmentDTO(departmentRepository.save(department));
  }
}
