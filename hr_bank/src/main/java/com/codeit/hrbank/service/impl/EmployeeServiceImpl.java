package com.codeit.hrbank.service.impl;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.BinaryContent;
import com.codeit.hrbank.entity.ChangeLog;
import com.codeit.hrbank.entity.ChangeLogType;
import com.codeit.hrbank.entity.Department;
import com.codeit.hrbank.entity.Employee;
import com.codeit.hrbank.entity.EmployeeStatus;
import com.codeit.hrbank.entity.History;
import com.codeit.hrbank.mapper.EmployeeMapper;
import com.codeit.hrbank.mapper.PageResponseMapper;
import com.codeit.hrbank.repository.BinaryContentRepository;
import com.codeit.hrbank.repository.ChangeLogRepository;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.repository.EmployeeRepository;
import com.codeit.hrbank.repository.HistoryRepository;
import com.codeit.hrbank.service.EmployeeService;
import com.codeit.hrbank.storage.BinaryContentStorage;
import com.codeit.hrbank.util.ChangeLogUtils;
import jakarta.persistence.EntityManager;
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
  private final ChangeLogRepository changeLogRepository;
  private final HistoryRepository historyRepository;

  private final EmployeeMapper employeeMapper;
  private final PageResponseMapper pageResponseMapper;

  private final EntityManager entityManager;

  @Override
  @Transactional
  public EmployeeDTO createEmployee(EmployeeCreateRequest request, MultipartFile profile, String ipAddress) {
    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("중복되는 이메일이 있습니다.");
    }

    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    int year = request.hireDate().atStartOfDay(ZoneId.systemDefault()).getYear();
    String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    String newEmployeeNumber = String.format("EMP-%d-%s", year, randomPart);

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder()
          .name(profile.getOriginalFilename())
          .size(profile.getSize())
          .contentType(profile.getContentType())
          .build();
      binaryContentRepository.save(binaryContent);
      try {
        binaryContentStorage.putFile(binaryContent.getId(), profile.getBytes(), binaryContent.getName());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    Instant hireDate = request.hireDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
    Employee employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .employeeNumber(newEmployeeNumber)
        .department(department)
        .position(request.position())
        .hireDate(hireDate)
        .status(EmployeeStatus.ACTIVE)
        .binaryContent(binaryContent)
        .build();
    Employee savedEmployee = employeeRepository.save(employee);

    // ChangeLog + History
    ChangeLog changeLog = ChangeLogUtils.createChangeLog(ChangeLogType.CREATED, ipAddress, request.memo(), savedEmployee);
    changeLogRepository.save(changeLog);

    List<History> histories = ChangeLogUtils.createHistoriesForCreate(changeLog, savedEmployee);
    historyRepository.saveAll(histories);

    return employeeMapper.toDTO(savedEmployee);
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
  public EmployeeDTO updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile profile, String ipAddress) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다"));

    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다."));

    if (employeeRepository.existsByEmailAndIdNot(request.email(), employeeId)) {
      throw new IllegalArgumentException("중복되는 이메일이 있습니다.");
    }

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder()
          .name(profile.getOriginalFilename())
          .size(profile.getSize())
          .contentType(profile.getContentType())
          .build();
      binaryContentRepository.save(binaryContent);
      try {
        binaryContentStorage.putFile(binaryContent.getId(), profile.getBytes(), binaryContent.getName());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    Employee updatedEmployee = employee.toBuilder()
        .name(request.name() != null ? request.name() : employee.getName())
        .email(request.email() != null ? request.email() : employee.getEmail())
        .department(department)
        .position(request.position() != null ? request.position() : employee.getPosition())
        .hireDate(employee.getHireDate())
        .binaryContent(binaryContent != null ? binaryContent : employee.getBinaryContent())
        .build();

    Employee savedEmployee = employeeRepository.save(updatedEmployee);

    ChangeLog changeLog = ChangeLogUtils.createChangeLog(ChangeLogType.UPDATED, ipAddress, request.memo(), savedEmployee);
    changeLogRepository.save(changeLog);

    List<History> histories = ChangeLogUtils.createHistoriesForUpdate(changeLog, employee, savedEmployee);
    if (!histories.isEmpty()) {
      historyRepository.saveAll(histories);
    }

    return employeeMapper.toDTO(savedEmployee);
  }

  @Override
  @Transactional
  public void deleteEmployee(Long employeeId, String ipAddress) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

    ChangeLog changeLog = ChangeLogUtils.createChangeLog(ChangeLogType.DELETED, ipAddress, null, employee);
    changeLogRepository.save(changeLog);

    List<History> histories = ChangeLogUtils.createHistoriesForDelete(changeLog, employee);
    historyRepository.saveAll(histories);

    employeeRepository.deleteById(employeeId);

    if (employee.getBinaryContent() != null) {
      binaryContentRepository.deleteById(employee.getBinaryContent().getId());
      binaryContentStorage.deleteFile(employee.getBinaryContent().getId());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponse<EmployeeDTO> findAllByPart(String nameOrEmail, String employeeNumber,
      String departmentName, String position, Instant hireDateFrom, Instant hireDateTo,
      EmployeeStatus status, Long idAfter, Integer size, String sortField, String sortDirection) {

    List<Employee> employees = employeeRepository.findAllQEmployeesPart(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, idAfter, size, sortField, sortDirection
    );

    boolean hasNext = employees.size() > size;
    if (hasNext) {
      employees = employees.subList(0, size);
    }

    List<EmployeeDTO> employeeDTOs = employees.stream()
        .map(employeeMapper::toDTO)
        .toList();

    Long nextIdAfter = null;
    String nextCursor = null;
    if (!employees.isEmpty()) {
      nextIdAfter = employees.get(employees.size() - 1).getId();
      nextCursor = String.valueOf(nextIdAfter);
    }

    return pageResponseMapper.fromCursor(employeeDTOs, size, nextCursor, nextIdAfter, hasNext);
  }
}
