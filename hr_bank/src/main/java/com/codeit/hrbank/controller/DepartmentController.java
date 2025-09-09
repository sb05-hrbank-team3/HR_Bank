package com.codeit.hrbank.controller;


import com.codeit.hrbank.dto.data.DepartmentDTO;
import com.codeit.hrbank.dto.request.DepartmentCreateRequest;
import com.codeit.hrbank.dto.request.DepartmentUpdateRequest;
import com.codeit.hrbank.repository.DepartmentRepository;
import com.codeit.hrbank.service.DepartmentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
public class DepartmentController {

  private final DepartmentService departmentService;


  @PostMapping("")
  public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentCreateRequest request){
      DepartmentDTO departmentDTO =departmentService.create(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(departmentDTO);

  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDTO> findById(@PathVariable Long id){

    return departmentService.findById(id)
        .map(dto -> ResponseEntity.ok(dto))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id){
    departmentService.deleteById(id);
    return ResponseEntity.ok("부서가 삭제되었습니다.");

  }


  @PatchMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DepartmentUpdateRequest request){
    return ResponseEntity.ok(departmentService.update(id, request));
  }



}
