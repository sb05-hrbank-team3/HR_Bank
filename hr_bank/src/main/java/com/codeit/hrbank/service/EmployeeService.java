package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.Instant;
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
        EmployeeStatus status,
        Long idAfter,
        Integer size,
        String sortField,
        String sortDirection
    );

}
