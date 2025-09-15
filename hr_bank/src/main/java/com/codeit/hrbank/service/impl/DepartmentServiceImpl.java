package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.mapper.DepartmentMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final DepartmentMapper departmentMapper;
  private final EmployeeRepository employeeRepository;


  @Override
  @Transactional
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
  @Transactional
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
            .establishedDate(d.getEstablishedDate())
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
  @Transactional
  public Optional<DepartmentDTO> findById(Long id) {
    return departmentRepository.findById(id).map(departmentMapper::toDepartmentDTO);
  }

  @Override
  @Transactional
  public void deleteById(Long id) throws IllegalStateException {
    Department department = departmentRepository
        .findById(id).orElseThrow(() -> new NoSuchElementException("삭제 대상 부서가 없습니다: " + id));

    List<Employee> employees= employeeRepository.findAll();

    boolean exists = employees.stream()
        .anyMatch(e -> e.getDepartment().getId().equals(id));

    if (exists) {
      throw new IllegalStateException("소속된 직원이 1명 이상이면 삭제가 불가능합니다");
    }


    departmentRepository.deleteById(id);

  }

  @Override
  @Transactional
  public DepartmentDTO update(Long id, DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다: " + id));

    if(departmentRepository.existsByName(request.name())){
      throw new IllegalArgumentException("부서명은 중복될 수 없습니다.");
    }

    Department build = department.toBuilder()
        .name(request.name())
        .description(request.description())
        .establishedDate(request.establishedDate())
        .build();

    Department save = departmentRepository.save(build);

    return departmentMapper.toDepartmentDTO(save);
  }
}
