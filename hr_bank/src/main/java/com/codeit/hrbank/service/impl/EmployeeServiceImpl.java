package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.CursorPageResponse;
import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.mapper.EmployeeMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.service.EmployeeService;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional(readOnly = true)
  public EmployeeDTO findById(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId).orElseThrow(
        () -> new NoSuchElementException("회원이 존재하지 않습니다"));

    return employeeMapper.toDTO(employee);
  }

  @Override
  @Transactional
  public EmployeeDTO createEmployee(EmployeeCreateRequest request, MultipartFile profile) {
    Department department = departmentRepository.findById(request.departmentId()).orElseThrow(
        () -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    // employeeNumber 생성 (EMP-YYYY-XXX)
    int year = request.hireDate().atZone(ZoneId.systemDefault()).getYear();
    String lastEmployeeNumber = employeeRepository.findLastEmployeeNumberByYear(year);

    int nextSequence = 1;
    if (lastEmployeeNumber != null) {
      String[] parts = lastEmployeeNumber.split("-");
      nextSequence = Integer.parseInt(parts[2]) + 1;
    }

    String newEmployeeNumber = String.format("EMP-%d-%03d", year, nextSequence);

    Employee employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .employeeNumber(newEmployeeNumber)
        .department(department)
        .position(request.position())
        .hireDate(request.hireDate())
        .status(EmployeeStatus.ACTIVE)
        // 메모는 어따가 넣어?
        .build();

    // profile 삽입하는것 사용해야함.
    Employee save = employeeRepository.save(employee);

    return employeeMapper.toDTO(save);
  }

  @Override
  public EmployeeDTO updateEmployee(Long employeeId, EmployeeUpdateRequest request,
      MultipartFile profile) {
    Department department = departmentRepository.findById(request.departmentId()).orElseThrow(
        () -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    Employee employee = employeeRepository.findById(employeeId).orElseThrow(
        () -> new NoSuchElementException(" 회원이 존재하지 않습니다"));

    Employee updateEmployee = employee.toBuilder()
        .name(request.name())
        .email(request.email())
        .department(department)
        .position(request.position())
        .hireDate(request.hireDate())
        // 메모는 어따가 넣어?
        .build();
    // profile 삽입하는것 사용해야함.

    Employee save = employeeRepository.save(updateEmployee);
    return employeeMapper.toDTO(save);
  }

  @Override
  public void deleteEmployee(Long employeeId) {
    if (!employeeRepository.existsById(employeeId)) {
      throw new NoSuchElementException("회원이 존재하지 않습니다.");
    }

    employeeRepository.deleteById(employeeId);
  }

  @Override
  public CursorPageResponse<EmployeeDTO> findAllByPart(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      Instant hireDateFrom,
      Instant hireDateTo,
      EmployeeStatus status,
      Pageable pageable) {

    // 1. 데이터 조회 (필드별 파라미터 전달)
    List<EmployeeDTO> employees = employeeRepository.findAllQEmployeesPart(
        nameOrEmail, employeeNumber, departmentName, position, hireDateFrom, hireDateTo, status,
        pageable);

    // 2. 다음 페이지 커서 계산
    Long nextCursor = null;
    if (!employees.isEmpty()) {
      nextCursor = employees.get(employees.size() - 1).id(); // 마지막 직원 ID를 커서로 사용
    }

    // 3. CursorPageResponse 생성
    return CursorPageResponse.<EmployeeDTO>builder()
        .content(employees)
        .nextCursor(nextCursor)
        .build();
  }


}
