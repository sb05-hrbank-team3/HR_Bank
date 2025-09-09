package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.CursorPageResponse;
import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    EmployeeDTO findById(Long employeeId);

    EmployeeDTO createEmployee(EmployeeCreateRequest request, MultipartFile profile);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile profile);

    void deleteEmployee(Long employeeId);

    CursorPageResponse<EmployeeDTO> findAllByPart(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        Instant hireDateFrom,
        Instant hireDateTo,
        String status,
        Pageable pageable);

}
