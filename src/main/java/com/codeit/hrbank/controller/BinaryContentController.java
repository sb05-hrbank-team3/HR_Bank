package com.codeit.hrbank.controller;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
import com.codeit.hrbank.dto.response.BinaryContentResponse;
import com.codeit.hrbank.mapper.BinaryContentMapper;
import com.codeit.hrbank.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService service;

  @PostMapping(consumes = {"multipart/form-data"})
  public BinaryContentResponse upload(@RequestPart("file") MultipartFile file) {
    BinaryContentDTO dto = service.create(file);
    return BinaryContentMapper.toResponse(dto);
  }

  @GetMapping("/{id}")
  public BinaryContentResponse get(@PathVariable Long id) {
    return BinaryContentMapper.toResponse(service.get(id));
  }

  @GetMapping
  public List<BinaryContentResponse> list(@RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Long idAfter) {
    return service.list(size, idAfter).stream().map(BinaryContentMapper::toResponse).toList();
  }

  @PatchMapping("/{id}")
  public BinaryContentResponse updateName(@PathVariable Long id,
      @RequestBody BinaryContentUpdateRequest request) {
    return BinaryContentMapper.toResponse(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    return service.download(id);
  }
}
