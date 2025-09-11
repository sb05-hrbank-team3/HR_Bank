package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.mapper.DepartmentMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.service.DepartmentService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final DepartmentMapper departmentMapper;


  @Override
  public DepartmentDTO create(DepartmentCreateRequest request) {
    if(departmentRepository.existsByName(request.name())){
      throw new IllegalArgumentException("부서명은 중복될 수 없습니다.");
    }

    Department department = Department.builder()
        .name(request.name())
        .description(request.description())
        .establishedDate(request.establishedDate())
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

    // hasNext 판별 및 초과분 제거
    boolean hasNext = departments.size() > size;
    if (hasNext) {
      departments = departments.subList(0, size);
    }

    List<Long> deptIds = departments.stream().map(Department::getId).toList();
    Map<Long, Long> employeeCounts = departmentRepository.findEmployeeCountsByDepartmentIds(
        deptIds);

    // DTO 변환 + Employee count 매핑
    List<DepartmentDTO> content = departments.stream()
        .map(d -> DepartmentDTO.builder()
            .id(d.getId())
            .name(d.getName())
            .description(d.getDescription())
            .employeeCount(employeeCounts.getOrDefault(d.getId(), 0L)) // null이면 0으로
            .build())
        .toList();

    // 다음 cursor 계산
    Long nextIdAfter = !departments.isEmpty() ? departments.get(departments.size() - 1).getId() : null;
    String nextCursor = nextIdAfter != null ? String.valueOf(nextIdAfter) : null;

    // CursorPageResponse 반환
    return pageResponseMapper.fromCursor(content, size, nextCursor, nextIdAfter, hasNext);
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
