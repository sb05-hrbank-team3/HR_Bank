package com.codeit.hrbank.config;

import com.codeit.hrbank.dto.data.BinaryContentDTO;
import com.codeit.hrbank.dto.request.BinaryContentUpdateRequest;
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

  private final BinaryContentService binaryContentService;

  @PostMapping(consumes = {"multipart/form-data"})
  public BinaryContentDTO upload(@RequestPart("file") MultipartFile file) {
    BinaryContentDTO dto = binaryContentService.create(file);
    return dto;
  }

  @GetMapping("/{id}")
  public BinaryContentDTO get(@PathVariable Long id) {
    return binaryContentService.get(id);
  }

  @GetMapping
  public List<BinaryContentDTO> list(@RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) Long idAfter) {
    return binaryContentService.list(size, idAfter);
  }

  @PatchMapping("/{id}")
  public BinaryContentDTO updateName(@PathVariable Long id,
      @RequestBody BinaryContentUpdateRequest request) {
    return binaryContentService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    binaryContentService.delete(id);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    return binaryContentService.download(id);
  }
}
