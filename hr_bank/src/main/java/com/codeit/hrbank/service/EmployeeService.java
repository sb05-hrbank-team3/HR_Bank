package com.codeit.hrbank.service;

import com.codeit.hrbank.dto.data.EmployeeDTO;
import com.codeit.hrbank.dto.request.EmployeeCreateRequest;
import com.codeit.hrbank.dto.request.EmployeeUpdateRequest;
import com.codeit.hrbank.dto.response.CursorPageResponse;
import com.codeit.hrbank.entity.EmployeeStatus;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    EmployeeDTO findById(Long employeeId);

    EmployeeDTO createEmployee(EmployeeCreateRequest request, MultipartFile profile, String ipAddress);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile profile, String ipAddress);

    void deleteEmployee(Long employeeId, String ipAddress);

    CursorPageResponse<EmployeeDTO> findAllByPart(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        EmployeeStatus status,
        Long idAfter,
        Integer size,
        String sortField,
        String sortDirection
    );

}
