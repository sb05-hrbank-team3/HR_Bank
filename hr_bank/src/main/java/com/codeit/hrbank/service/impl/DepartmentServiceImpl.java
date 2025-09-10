package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.mapper.DepartmentMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.service.DepartmentService;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
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
    if(departmentRepository.existsByName(request.name())){
      throw new IllegalArgumentException("부서명은 중복될 수 없습니다.");
    }

    Instant date = request.establishedDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
    Department department = Department.builder()
        .name(request.name())
        .description(request.description())
        .establishedDate(date)
        .build();

    Department save = departmentRepository.save(department);
    return departmentMapper.toDTO(save);
  }

  @Override
  public CursorPageResponse<DepartmentDTO> findAll(
      String nameOrDescription,
      Long idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection
  ) {
    // 데이터 조회
    List<Department> departments = Optional.ofNullable(
        departmentRepository.findAndSortDepartments(
            nameOrDescription, cursor, idAfter, size, sortField, sortDirection
        )
    ).orElse(Collections.emptyList());

    // DTO 변환
    List<DepartmentDTO> content = departments.stream()
        .map(departmentMapper::toDTO)
        .toList();

    // 다음 커서 구하기 (예: 마지막 ID, 시간 등 기준으로)
    String nextCursor = content.isEmpty() ? null : String.valueOf(content.get(content.size() - 1).id());

    // CursorPageResponse 반환
    return CursorPageResponse.<DepartmentDTO>builder()
        .content(content)
        .size(size)
        .nextCursor(nextCursor)
        .hasNext(content.size() == size)
        .build();
  }


  @Override
  public Optional<DepartmentDTO> findById(Long id) {
    return departmentRepository.findById(id).map(departmentMapper::toDepartmentDTO);
  }

  @Override
  public void deleteById(Long id) {
    Department department = departmentRepository
        .findById(id).orElseThrow(() -> new NoSuchElementException("삭제 대상 부서가 없습니다: " + id));
    departmentRepository.deleteById(id);

  }

  @Override
  public DepartmentDTO update(Long id, DepartmentUpdateRequest request) {
    if(departmentRepository.existsByName(request.name())){
      throw new IllegalArgumentException("부서명은 중복될 수 없습니다.");
    }

    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다: " + id));

    return departmentMapper.toDepartmentDTO(departmentRepository.save(department));
  }
}
