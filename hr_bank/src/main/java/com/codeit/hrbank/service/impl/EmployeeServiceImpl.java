package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.mapper.EmployeeMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.BinaryContentRepository;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.service.EmployeeService;
import com.codeit.hrbank.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  private final EmployeeMapper employeeMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public EmployeeDTO createEmployee(EmployeeCreateRequest request, MultipartFile profile) {
    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("중복되는 이메일이 있습니다.");
    }

    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    // employeeNumber 생성 (EMP-YYYY-UUID)
    int year = request.hireDate().atStartOfDay(ZoneId.systemDefault()).getYear();
    String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    String newEmployeeNumber = String.format("EMP-%d-%s", year, randomPart);

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder().name(profile.getOriginalFilename())
          .size(profile.getSize()).contentType(profile.getContentType()).build();
      binaryContentRepository.save(binaryContent);
      try {
        binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    // 꼼수 바꿔야하면 바꿔야함.
    Instant hireDate = request.hireDate().atStartOfDay(ZoneId.systemDefault()).toInstant();

    Employee employee = Employee.builder().name(request.name()).email(request.email())
        .employeeNumber(newEmployeeNumber).department(department).position(request.position())
        .hireDate(hireDate).status(EmployeeStatus.ACTIVE).binaryContent(binaryContent)
        // 메모는 어따가 넣어?
        .build();

    Employee save = employeeRepository.save(employee);

    return employeeMapper.toDTO(save);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDTO findById(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다"));

    return employeeMapper.toDTO(employee);
  }

  @Override
  @Transactional
  public EmployeeDTO updateEmployee(Long employeeId, EmployeeUpdateRequest request,
      MultipartFile profile) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다"));

    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("중복되는 이메일이 있습니다.");
    }

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder().name(profile.getOriginalFilename())
          .size(profile.getSize()).contentType(profile.getContentType()).build();
      binaryContentRepository.save(binaryContent);
      try {
        binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    // 꼼수 바꿔야하면 바꿔야함.
    // 입사일 업데이트: null이면 기존 값 유지
    Instant hireDate = employee.getHireDate();

    Employee updateEmployee = employee.toBuilder()
        .name(request.name() != null ? request.name() : employee.getName())
        .email(request.email() != null ? request.email() : employee.getEmail())
        .department(department != null ? department : employee.getDepartment())
        .position(request.position() != null ? request.position() : employee.getPosition())
        .hireDate(hireDate != null ? hireDate: employee.getHireDate())
        .binaryContent(binaryContent != null ? binaryContent : employee.getBinaryContent())
        // 메모는 없으면 기존 메모 유지
        .build();

    Employee save = employeeRepository.save(updateEmployee);
    return employeeMapper.toDTO(save);
  }

  @Override
  public void deleteEmployee(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

    employeeRepository.deleteById(employee.getId());

    binaryContentRepository.deleteById(employee.getBinaryContent().getId());
    binaryContentStorage.delete(employee.getBinaryContent().getId());
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponse<EmployeeDTO> findAllByPart(String nameOrEmail, String employeeNumber,
      String departmentName, String position, Instant hireDateFrom, Instant hireDateTo,
      EmployeeStatus status, Long idAfter, Integer size, String sortField, String sortDirection) {

    // 1. Entity 조회 (QueryDSL)
    List<Employee> employees = employeeRepository.findAllQEmployeesPart(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, idAfter, size, sortField, sortDirection
    );

    // 2. Entity → DTO 변환
    List<EmployeeDTO> employeeDTOs = employees.stream()
        .map(employeeMapper::toDTO)
        .toList();

    // 3. 다음 커서 계산
    String nextCursor = null;
    if (!employeeDTOs.isEmpty()) {
      nextCursor = employeeDTOs.get(employeeDTOs.size() - 1).id().toString();
    }

    // 4. CursorPageResponse 생성
    return pageResponseMapper.fromCursor(employeeDTOs, size, nextCursor);
  }
}
